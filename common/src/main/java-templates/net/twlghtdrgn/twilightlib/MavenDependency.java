package net.twlghtdrgn.twilightlib;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("all")
/**
 * A list of dependencies used in the library.
 * @since 0.23.3
 */
public class MavenDependency {
    private MavenDependency() {}

    public static final Artifact CONFIGURATE = new Artifact("org.spongepowered", "configurate-yaml", "${configurate.version}");
    public static final Artifact JEDIS = new Artifact("redis.clients", "jedis", "${jedis.version}");

    public static class Database {
        public static class Provider {
            public static final Artifact H2 = new Artifact("com.h2database", "h2", "${h2.version}");
            public static final Artifact POSTGRESQL = new Artifact("org.postgresql", "postgresql", "${postgresql.version}");
            public static final Artifact MARIADB = new Artifact("org.mariadb.jdbc", "mariadb-java-client", "${mariadb.version}");
        }

        public static class Helper {
            public static final Artifact HIKARICP = new Artifact("com.zaxxer", "HikariCP", "${hikaricp.version}");
            public static final Artifact ORMLITE = new Artifact("com.j256.ormlite", "ormlite-jdbc", "${ormlite.version}");

            public static class Flyway {
                public static final Artifact CORE = new Artifact("org.flywaydb","flyway-core", "${flyway.version}");
                public static final Artifact POSTGRESQL = new Artifact("org.flywaydb","flyway-database-postgresql", "${flyway.version}");
                public static final Artifact MYSQL = new Artifact("org.flywaydb","flyway-mysql", "${flyway.version}");
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Artifact {
        private final String groupId;
        private final String artifactId;
        private final String version;

        public String getGradleCoords() {
            return String.join(":", groupId, artifactId, version);
        }
    }
}
