package net.twlghtdrgn.twilightlib.api.database;

import net.twlghtdrgn.twilightlib.api.sql.IConnector;

import java.util.List;

public interface IDatabase {
    IConnector getConnector();
    void createTable(Class<?> table);
    <T> List<Object> getTable(Class<T> table, String col, Object value);
    void saveTable(Object table);
    void shutdown();
}
