package net.twlghtdrgn.twilightlib;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@SuppressWarnings("all")
/**
 * A list of dependencies used in the library.
 * @since 0.23.3
 */
public final class MavenDependency {
    private MavenDependency() {}

    private static final Artifact configurate = new Artifact("org.spongepowered", "configurate-yaml", "${configurate.version}");
    private static final Artifact jedis = new Artifact("redis.clients", "jedis", "${jedis.version}");

    public static class Database {
        @Getter
        public static class Provider {
            private static final Artifact H2 = new Artifact("com.h2database", "h2", "${h2.version}");
            private static final Artifact postgreSql = new Artifact("org.postgresql", "postgresql", "${postgresql.version}");
            private static final Artifact mariaDb = new Artifact("org.mariadb.jdbc", "mariadb-java-client", "${mariadb.version}");
        }

        @Getter
        public static class Helper {
            private static final Artifact hikariCP = new Artifact("com.zaxxer", "HikariCP", "${hikaricp.version}");
            private static final Artifact ormLite = new Artifact("com.j256.ormlite", "ormlite-jdbc", "${ormlite.version}");

            @Getter
            public static class Flyway {
                private static final Artifact core = new Artifact("org.flywaydb","flyway-core", "${flyway.version}");
                private static final Artifact postgreSql = new Artifact("org.flywaydb","flyway-database-postgresql", "${flyway.version}");
                private static final Artifact mySql = new Artifact("org.flywaydb","flyway-mysql", "${flyway.version}");
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
