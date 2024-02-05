package net.twlghtdrgn.twilightlib.api.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A MySql (MariaDB) connector using HikariCP
 * @author TwlghtDrgn
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class HikariConnector implements IConnector {
    private final HikariDataSource dataSource;
    private final Configuration<MariaDBConfig> sqlConfig;
    private final ILibrary library;

    public HikariConnector(@NotNull ILibrary library) throws ConfigurateException, IllegalStateException {
        this.library = library;
        sqlConfig = new Configuration<>(library, MariaDBConfig.class, "mariadb");
        sqlConfig.reload();

        String host = sqlConfig.get().getHostname();
        String port = sqlConfig.get().getPort();
        String database = sqlConfig.get().getDatabase();
        String user = sqlConfig.get().getUser();
        String password = sqlConfig.get().getPassword();

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

        library.log().info("Testing database connection...");
        try (Connection conn = getConnection()) {
            library.log().info("Database connection: OK");
        } catch (SQLException e) {
            library.log().error("Unable to connect. Is credentials wrong?", e);
            throw new IllegalStateException("MariaDB cannot be loaded");
        }
    }

    /**
     * Get a connection to the DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void shutdown() {
        library.log().info("Shutting down MariaDB");
        if (dataSource != null) dataSource.close();
    }

    @Data
    @ConfigSerializable
    protected static class MariaDBConfig {
        private String hostname = "127.0.0.1";
        private String port = "3306";
        private String database = "minecraft";
        private String user = "minecraft";
        private String password = "p@ssw0rd";
    }
}
