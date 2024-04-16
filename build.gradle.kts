@file:Suppress("PropertyName")

import nu.studer.gradle.jooq.JooqGenerate
import org.jooq.codegen.JavaGenerator
import org.jooq.meta.h2.H2Database
import org.jooq.meta.jaxb.Logging

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("org.flywaydb.flyway") version "9.20.0"
    id("nu.studer.jooq") version "8.2"
}

group = "me.groot"
version = "1.0"

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.h2database:h2:2.2.224")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val project_reactor_version: String by project
val r2dbc_h2_version: String by project
val jooq_version: String by project
val flyway_version: String by project
val h2_version: String by project

dependencies {
    implementation("io.projectreactor:reactor-core:$project_reactor_version")
    implementation("io.r2dbc:r2dbc-h2:$r2dbc_h2_version")
    implementation("org.jooq:jooq:$jooq_version")
    implementation("org.flywaydb:flyway-core:$flyway_version")
    jooqGenerator("com.h2database:h2:$h2_version")
}

val databaseUrl = "jdbc:h2:${project.layout.buildDirectory.get().asFile}/schema-gen/database/database"
flyway {
    driver = "org.h2.Driver"
    user = "SA"
    password = ""
    url = databaseUrl
    validateMigrationNaming = true
    cleanOnValidationError = true
    table = "schema_history"
}

flyway.cleanDisabled = false

jooq {
    version.set(jooq_version)

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.h2.Driver"
                    url = databaseUrl
                    user = "SA"
                    password = ""
                }

                generator.apply {
                    name = JavaGenerator::class.java.canonicalName
                    database.apply {
                        name = H2Database::class.java.canonicalName
                        inputSchema = "PUBLIC"
                        includes = ".*"
                        excludes = "(?i)INFORMATION_SCHEMA\\\\..*(?-i)|(?i)SYSTEM_\\\\..*(?-i)"
                        schemaVersionProvider = "SELECT :schema_name || '_' || MAX(\"version\") FROM \"schema_history\""
                        generate.withJavadoc(true)
                            .withComments(true)
                            .withDaos(true)
                            .withRecords(true)
                            .withPojos(false)
                        target.withPackageName("me.groot.downloadmanager.jooq.codegen")
                            .withDirectory("${project.layout.buildDirectory.get().asFile}/schema-gen/jooq")
                    }
                }
            }
        }
    }
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    compileJava {
        dependsOn(flywayMigrate)
        dependsOn("generateJooq")
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveFileName.set("DownloadManager-$version.jar")
        minimize()
    }

    jar {
        manifest {
            attributes["Implementation-Title"] = "DownloadManager"
            attributes["Implementation-Version"] = version
            attributes["Main-Class"] = "me.groot.downloadmanager.Application"
        }
    }

    named<JooqGenerate>("generateJooq") {
        allInputsDeclared.set(true)
        outputs.upToDateWhen {
            layout.buildDirectory.get().asFile.resolve("schema-gen").lastModified() <= layout.buildDirectory.get().asFile.resolve("schema-gen/jooq").lastModified()
        }
    }
}