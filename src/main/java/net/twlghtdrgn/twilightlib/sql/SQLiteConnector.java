package net.twlghtdrgn.twilightlib.sql;

import net.twlghtdrgn.twilightlib.TwilightPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 */
@SuppressWarnings("unused")
public class SQLiteConnector implements SQL {
    private final TwilightPlugin plugin;
    public SQLiteConnector(@NotNull TwilightPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Using SQLite driver, no load is required");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/sqlite.db");
    }
}
