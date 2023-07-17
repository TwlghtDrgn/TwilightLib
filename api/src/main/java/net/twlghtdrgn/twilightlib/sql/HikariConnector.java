package net.twlghtdrgn.twilightlib.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import net.twlghtdrgn.twilightlib.ILibrary;
import net.twlghtdrgn.twilightlib.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private static ILibrary library;

    public HikariConnector(@NotNull ILibrary library) {
        this.library = library;
        SQLConfig config = new SQLConfig("database.yml",null);

        String host = config.config.hostname;
        String port = config.config.port;
        String database = config.config.database;
        String user = config.config.user;
        String password = config.config.password;

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

        library.getLogger().info("Loaded MariaDB driver");
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

    protected static class SQLConfig extends AbstractConfig {
        private Config config;

        public SQLConfig(String configName, Class<?> configClass) {
            super(configName, Config.class);
        }

        @Override
        public void reload() throws IOException {
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
