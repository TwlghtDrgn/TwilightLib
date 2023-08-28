package net.twlghtdrgn.twilightlib.api.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A MySql (MariaDB) connector using HikariCP
 */
@SuppressWarnings("unused")
public class HikariConnector implements SQL {
    private final HikariDataSource dataSource;
    private final ILibrary library;

    public HikariConnector(@NotNull ILibrary library) {
        this.library = library;
        SQLConfig config = new SQLConfig("database.yml",null);
        try {
            config.reload();
        } catch (IOException e) {
            throw new NullPointerException("Unable to load db config");
        }

        String host = config.getConfig().getHostname();
        String port = config.getConfig().getPort();
        String database = config.getConfig().getDatabase();
        String user = config.getConfig().getUser();
        String password = config.getConfig().getPassword();

        dataSource = new HikariDataSource();

        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource.addDataSourceProperty("useUnicode",true);
        dataSource.addDataSourceProperty("characterEncoding","utf8");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Getter
    protected class SQLConfig extends AbstractConfig {
        private Config config;

        public SQLConfig(String configName, Class<?> configClass) {
            super(configName, Config.class);
        }

        @Override
        public void reload() throws ConfigurateException {
            config = (Config) library.getConfigLoader().load(this);
        }

        @Data
        @ConfigSerializable
        public static class Config {
            private String hostname = "127.0.0.1";
            private String port = "3306";
            private String database = "ChangeMe";
            private String user = "admin";
            private String password = "password";
        }
    }
}
