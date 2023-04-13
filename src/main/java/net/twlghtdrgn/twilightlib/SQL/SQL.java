package net.twlghtdrgn.twilightlib.SQL;

import net.twlghtdrgn.twilightlib.Exception.ConfigLoadException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface SQL {
    void load() throws SQLException, ConfigLoadException, IOException;
    Connection getConnection() throws SQLException;
}
