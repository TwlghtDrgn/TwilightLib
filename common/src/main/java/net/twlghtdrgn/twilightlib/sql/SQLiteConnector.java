package net.twlghtdrgn.twilightlib.sql;

import net.twlghtdrgn.twilightlib.ILibrary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An SQLite Database connector
 * @author TwlghtDrgn
 * @since 0.0.1
 * @deprecated can only work on Paper (dependency missing on Velocity). Use {@link ORMConnector} instead
 */
@SuppressWarnings("unused")
@Deprecated(since = "0.23.3")
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
