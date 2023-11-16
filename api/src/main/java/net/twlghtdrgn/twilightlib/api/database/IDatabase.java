package net.twlghtdrgn.twilightlib.api.database;

import java.util.List;

public interface IDatabase {
    void createTable(Class<?> table);
    <T> List<Object> getTable(Class<T> table, String col, Object value);
    void saveTable(Object table);
    void shutdown();
}
