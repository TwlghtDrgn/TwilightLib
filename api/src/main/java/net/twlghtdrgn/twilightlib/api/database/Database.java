package net.twlghtdrgn.twilightlib.api.database;

import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.database.annotation.*;
import net.twlghtdrgn.twilightlib.api.sql.HikariConnector;
import net.twlghtdrgn.twilightlib.api.sql.IConnector;
import net.twlghtdrgn.twilightlib.api.sql.SQLiteConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class Database implements IDatabase {
    private final IConnector connector;
    private final ILibrary library;
    private final boolean useMariaDB;
    public Database(ILibrary library, boolean isExternal) throws ConfigurateException, ClassNotFoundException {
        this.library = library;
        this.useMariaDB = isExternal;
        if (isExternal) this.connector = new HikariConnector(library);
        else this.connector = new SQLiteConnector(library);
    }

    @Override
    public void createTable(@NotNull Class<?> table) {
        try {
            if (table.getAnnotation(Table.class) == null) throw new IllegalArgumentException("Class " + table + " is not a table");
            final StringJoiner joiner = new StringJoiner(" ");
            joiner.add("CREATE TABLE IF NOT EXISTS");
            joiner.add(table.getSimpleName().toLowerCase());
            joiner.add("(");
            for (int i = 1; i <= table.getDeclaredFields().length; i++) {
                Field f = table.getDeclaredFields()[i - 1];
                joiner.add(f.getName().toLowerCase());
                joiner.add(getColumnType(f.getType()));

                if (f.getAnnotations().length < 1) continue;
                for (Annotation a:f.getAnnotations()) {
                    if (a.annotationType() == Length.class) joiner.add("(" + ((Length) a).length() + ")");
                    if (a.annotationType() == PrimaryKey.class) joiner.add("PRIMARY KEY");
                    if (a.annotationType() == Unique.class) joiner.add("UNIQUE");
                    if (a.annotationType() == AutoIncrement.class) joiner.add(useMariaDB ? "AUTO_INCREMENT" : "AUTOINCREMENT");
                }
                if (i < table.getDeclaredFields().length) joiner.add(",");
            }
            joiner.add(")");
            try (Connection conn = connector.getConnection();
                Statement st = conn.createStatement()) {
                st.execute(joiner.toString());
            }
        } catch (Exception e) {
            library.log().error("Unable to create a table {}: {}", e.getMessage(), table.getSimpleName().toLowerCase(), e);
        }
    }

    @Override
    public <T> List<Object> getTable(@NotNull Class<T> table, String col, Object value) {
        try {
            if (table.getAnnotation(Table.class) == null) throw new IllegalArgumentException("Class " + table + " is not a table");
            try (Connection conn = connector.getConnection();
                 PreparedStatement pst = conn.prepareStatement("SELECT * FROM " + table.getSimpleName().toLowerCase() + " WHERE " + col + " = ?")) {
                pst.setObject(1, value);
                return mapTable(table, pst.executeQuery());
            }
        } catch (SQLException e) {
            library.log().error("Unable to fetch a table {}: {}", table.getSimpleName().toLowerCase(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void saveTable(Object table) {
        try {
            if (table.getClass().getAnnotation(Table.class) == null) throw new IllegalArgumentException("Class " + table + " is not a table");
            final StringJoiner joiner = new StringJoiner(" ");
            joiner.add("INSERT INTO " + table.getClass().getSimpleName().toLowerCase() + " VALUES (");
            for (int i = 1; i <= table.getClass().getDeclaredFields().length; i++) {
                joiner.add("?");
                if (i < table.getClass().getDeclaredFields().length) joiner.add(",");
            }
            joiner.add(")");

            int unknown = 0;
            if (useMariaDB) joiner.add("ON DUPLICATE KEY UPDATE");
            else {
                joiner.add("ON CONFLICT("+ Arrays.stream(table.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(PrimaryKey.class) != null).findFirst().get().getName().toLowerCase() + ") DO UPDATE SET");
                unknown++;
            }
            for (int i = 1; i <= table.getClass().getDeclaredFields().length; i++) {
                Field field = table.getClass().getDeclaredFields()[i - 1];
                joiner.add(field.getName().toLowerCase());
                joiner.add("=");
                joiner.add("?");
                if (i < table.getClass().getDeclaredFields().length) joiner.add(",");
                unknown++;
            }

            try (Connection conn = connector.getConnection();
                PreparedStatement pst = conn.prepareStatement(joiner.toString())) {
                for (int i = 1; i <= table.getClass().getDeclaredFields().length; i++) {
                    table.getClass().getDeclaredFields()[i-1].setAccessible(true);
                    pst.setObject(i, table.getClass().getDeclaredFields()[i-1].get(table));
                    if (useMariaDB) {
                        pst.setObject((unknown/2 + i), table.getClass().getDeclaredFields()[i-1].get(table));
                    } else {
                        pst.setObject((unknown/2 + 1 + i), table.getClass().getDeclaredFields()[i-1].get(table));
                    }
                    table.getClass().getDeclaredFields()[i-1].setAccessible(false);
                }
                pst.executeUpdate();
            }
        } catch (Exception e) {
            library.log().error("Unable to save a table {}: {}", table.getClass().getSimpleName().toLowerCase(), e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {
        if (useMariaDB) ((HikariConnector) connector).shutdown();
    }

    private String getColumnType(@NotNull Class<?> clazz) {
        return switch (clazz.getSimpleName().toLowerCase()) {
            case "integer" -> "INTEGER";
            case "boolean" -> "BOOLEAN";
            case "double" -> useMariaDB ? "DOUBLE" : "REAL";
            case "long" -> useMariaDB ? "MEDIUMTEXT" : "NUMERIC";
            case "float" -> useMariaDB ? "FLOAT" : "REAL";
            default -> "TEXT";
        };
    }

    private <T> @Nullable List<Object> mapTable(Class<T> table, ResultSet response) {
        try {
            final List<Object> rows = new ArrayList<>();
            while (response.next()) {
                Object newTable = table.getConstructor().newInstance();
                for (int i = 0; i < table.getDeclaredFields().length; i++) {
                    Field field = newTable.getClass().getDeclaredFields()[i];
                    field.setAccessible(true);
                    field.set(newTable, response.getObject(i + 1));
                    field.setAccessible(false);
                }
                rows.add(newTable);
            }
            return rows;
        } catch (Exception e) {
            library.log().error("Unable to map a table {}: {}", table.getSimpleName().toLowerCase(), e.getMessage(), e);
            return null;
        }
    }
}
