package net.twlghtdrgn.twilightlib.api.sql;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQL {
    @Nullable
    Connection getConnection() throws SQLException;
}
