package net.twlghtdrgn.twilightlib.sql.config;

import lombok.Getter;

/**
 * Types for the database
 * @author TwlghtDrgn
 * @since 0.23.0
 */
@Getter
public enum DatabaseMode {
    H2("jdbc:h2:", org.h2.Driver.class.getName()),
    MARIADB("jdbc:mariadb:", org.mariadb.jdbc.Driver.class.getName()),
    POSTGRESQL("jdbc:postgresql:", org.postgresql.Driver.class.getName());

    private final String url;
    private final String driverClass;
    DatabaseMode(String url, String driverClass) {
        this.url = url;
        this.driverClass = driverClass;
    }
}
