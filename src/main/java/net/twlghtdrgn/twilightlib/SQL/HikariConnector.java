package net.twlghtdrgn.twilightlib.SQL;

import com.zaxxer.hikari.HikariDataSource;
import net.twlghtdrgn.twilightlib.Config.Config;
import net.twlghtdrgn.twilightlib.Exception.ConfigLoadException;
import net.twlghtdrgn.twilightlib.TwilightLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnector implements SQL {
    private static HikariDataSource dataSource;

    @Override
    public void load() throws ConfigLoadException, IOException {
        FileConfiguration sql = Config.load("sql.yml");
        String host, port, database, user, password;

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

        TwilightLib.getPlugin().getLogger().info("Loaded MariaDB driver");
    }

    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
