package net.twlghtdrgn.twilightlib.sql;

import net.twlghtdrgn.twilightlib.TwilightLibImpl;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 */
public class SQLiteConnector implements SQL {
    public SQLiteConnector() {
        TwilightLibImpl.getPlugin().getLogger().info("Using SQLite driver, no load is required");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + TwilightLibImpl.getPlugin().getDataFolder() + "/sqlite.db");
    }
}
