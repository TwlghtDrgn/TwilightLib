package net.twlghtdrgn.twilightlib.sql;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

public interface SQL {
    @Nullable
    Connection getConnection() throws SQLException;
}
