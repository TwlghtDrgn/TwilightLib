package net.twlghtdrgn.twilightlib.SQL;

import net.twlghtdrgn.twilightlib.TwilightLib;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements SQL{
    private static Connection conn;

    @Override
    public void load() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + TwilightLib.getPlugin().getDataFolder() + "/sqlite.db");
        TwilightLib.getPlugin().getLogger().info("Loaded SQLite driver");
    }

    @Override
    @Nullable
    public Connection getConnection() {
        return conn;
    }
}
