package net.twlghtdrgn.twilightlib.sql;

import net.twlghtdrgn.twilightlib.TwilightLib;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 */
public class SQLiteConnector implements SQL{
    /**
     * Allows to load a SQLite database
     */
    @Override
    public void load() {
        TwilightLib.getPlugin().getLogger().info("Using SQLite driver, no load is required");
    }

    private Connection get() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + TwilightLib.getPlugin().getDataFolder() + "/sqlite.db");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() {
        try {
            return get();
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(TwilightLib.getPlugin());
            e.printStackTrace();
            return null;
        }
    }
}
