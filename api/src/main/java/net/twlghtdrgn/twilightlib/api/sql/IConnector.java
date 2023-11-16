package net.twlghtdrgn.twilightlib.api.sql;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connector interface
 * @author TwlghtDrgn
 * @since 0.0.1
 */
public interface IConnector {
    @Nullable
    Connection getConnection() throws SQLException;
}
