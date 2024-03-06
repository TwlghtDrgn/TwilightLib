package net.twlghtdrgn.twilightlib.sql;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.zaxxer.hikari.HikariDataSource;
import net.twlghtdrgn.twilightlib.ILibrary;
import net.twlghtdrgn.twilightlib.config.Configuration;
import net.twlghtdrgn.twilightlib.sql.config.DatabaseMode;
import net.twlghtdrgn.twilightlib.sql.config.ORMConfig;
import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ORMLite database connector
 * @author TwlghtDrgn
 * @since 0.23.0
 */
@SuppressWarnings("unused")
public class ORMConnector implements IConnector {
    private final ILibrary library;
    private final Configuration<ORMConfig> config;
    private final HikariDataSource dataSource = new HikariDataSource();
    private ConnectionSource connectionSource;

    public ORMConnector(ILibrary library) throws IllegalStateException, ConfigurateException {
        this.library = library;
        this.config = new Configuration<>(library, ORMConfig.class,"database");
        config.reload();
    }

    /**
     * Reloads this connector
     */
    public void reload() throws Exception {
        library.log().info("Database is reloading..");
        config.reload();
        if (connectionSource != null) connectionSource.close();
        if (dataSource.isRunning()) dataSource.close();

        final ORMConfig.Credentials credentials = config.get().getCredentials();
        final ORMConfig.Pool pool = config.get().getPool();

        dataSource.setUsername(credentials.getUser());
        dataSource.setPassword(credentials.getPassword());
        dataSource.setMinimumIdle(pool.getMinimumIdle());
        dataSource.setMaximumPoolSize(pool.getMaximumPoolSize());
        dataSource.setIdleTimeout(pool.getIdleTimeout());

        final String url = config.get().getMode().getUrl() +
                (config.get().getMode() == DatabaseMode.H2 ?
                        "./" + library.getPath() + "/h2.db" :
                        "//" + credentials.getHostname() + ":" + credentials.getPort() + "/" + credentials.getDatabase());
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(config.get().getMode().getDriverClass());

        Flyway flyway = Flyway.configure(library.getClass().getClassLoader())
                .dataSource(dataSource)
                .locations("classpath:migrations")
                .load();
        flyway.migrate();

        connectionSource = new DataSourceConnectionSource(dataSource, url);
        library.log().info("Database reloading: success");
    }

    /**
     * @see IConnector
     */
    @Override
    public @Nullable Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * An ORMLite {@link ConnectionSource}
     */
    public @Nullable ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * @see IConnector
     */
    @Override
    public void shutdown() throws Exception {
        library.log().info("Database is shutting down..");
        if (connectionSource != null) connectionSource.close();
        if (dataSource.isRunning()) dataSource.close();
    }
}
