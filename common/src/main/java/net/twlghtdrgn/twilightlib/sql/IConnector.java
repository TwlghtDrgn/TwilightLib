package net.twlghtdrgn.twilightlib.sql;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connector interface
 * @author TwlghtDrgn
 * @since 0.0.1
 */
public interface IConnector {
    /**
     * A default JDBC connection.
     * Should be closed after usage
     */
    @Nullable
    Connection getConnection() throws SQLException;

    /**
     * Properly shuts down the connector
     */
    default void shutdown() throws Exception {}
}
