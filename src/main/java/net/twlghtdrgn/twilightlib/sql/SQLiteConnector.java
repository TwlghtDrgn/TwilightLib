package net.twlghtdrgn.twilightlib.sql;

import net.twlghtdrgn.twilightlib.TwilightLib;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 */
public class SQLiteConnector implements SQL{
    private Connection conn;

    /**
     * Allows to load a SQLite database
     */
    @Override
    public void load() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + TwilightLib.getPlugin().getDataFolder() + "/sqlite.db");
        TwilightLib.getPlugin().getLogger().info("Loaded SQLite driver");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() {
        return conn;
    }
}
