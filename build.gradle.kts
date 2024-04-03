@file:Suppress("PropertyName")

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.groot"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val rxjava_version: String by project

dependencies {
    implementation("io.reactivex.rxjava3:rxjava:$rxjava_version")
}

tasks {
    shadowJar {
        archiveFileName.set("DownloadManager-$version.jar")
        minimize()
    }
}