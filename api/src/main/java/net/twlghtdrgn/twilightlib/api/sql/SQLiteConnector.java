package net.twlghtdrgn.twilightlib.api.sql;

import net.twlghtdrgn.twilightlib.api.ILibrary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 * @author TwlghtDrgn
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class SQLiteConnector implements IConnector {
    private final ILibrary library;
    public SQLiteConnector(@NotNull ILibrary library) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.library = library;
        library.log().info("SQLite is loaded");
    }

    /**
     * Get a connection to DB
     * @return database connection
     */
    @Override
    @Nullable
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + library.getPath() + "/sqlite.db");
    }
}
