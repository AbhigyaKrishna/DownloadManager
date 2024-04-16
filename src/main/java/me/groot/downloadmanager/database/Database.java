package me.groot.downloadmanager.database;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;

import java.time.Duration;

public class Database {

    private final DatabaseSettings settings;
    private ConnectionFactory factory;
    private DSLContext context;

    public Database(DatabaseSettings settings) {
        this.settings = settings;
    }

    public void create() {
        factory = ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.HOST, settings.getHost())
                        .option(ConnectionFactoryOptions.PORT, settings.getPort())
                        .option(ConnectionFactoryOptions.DATABASE, settings.getDatabase())
                        .option(ConnectionFactoryOptions.USER, settings.getUser())
                        .option(ConnectionFactoryOptions.PASSWORD, settings.getPassword())
                        .option(ConnectionFactoryOptions.CONNECT_TIMEOUT, Duration.ofSeconds(30))
                        .option(ConnectionFactoryOptions.DRIVER, "org.h2.Driver")
                        .build()
        );
    }

    private DSLContext createContext(ConnectionFactory factory) {
        return new DefaultConfiguration()
                .set(factory)
                .set(SQLDialect.H2)
                .set(new Settings().withRenderSchema(false))
                .dsl();
    }

    public DSLContext getContext() {
        return getContext(false);
    }

    public DSLContext getContext(boolean createNew) {
        if (context == null || createNew) {
            context = createContext(factory);
        }
        return context;
    }

}
