package net.twlghtdrgn.twilightlib.sql;

import com.zaxxer.hikari.HikariDataSource;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.exception.ConfigLoadException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A MySql (MariaDB) connector using HikariCP
 */
@SuppressWarnings("unused")
public class HikariConnector implements SQL {
    private final HikariDataSource dataSource;

    public HikariConnector(@NotNull TwilightPlugin plugin) throws ConfigLoadException, IOException {
        FileConfiguration sql = plugin.getConfiguration().legacy("sql.yml");
        String host;
        String port;
        String database;
        String user;
        String password;

        host = sql.getString("database.hostname");
        port = sql.getString("database.port");
        user = sql.getString("database.user");
        password = sql.getString("database.password");
        database = sql.getString("database.database");

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

        plugin.getLogger().info("Loaded MariaDB driver");
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
}
