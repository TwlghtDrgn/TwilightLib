package net.twlghtdrgn.twilightlib.api.sql.config;

import lombok.Data;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Data
@ConfigSerializable
public class ORMConfig {
    private DatabaseMode mode = DatabaseMode.H2;
    private final Credentials credentials = new Credentials();
    private final Pool pool = new Pool();

    @Data
    @ConfigSerializable
    public static class Credentials {
        private String hostname = "127.0.0.1";
        private int port = 3306;
        private String database = "minecraft";
        private String user = "minecraft";
        private String password = "p@ssw0rd";
    }

    @Data
    @ConfigSerializable
    public static class Pool {
        private int minimumIdle = 5;
        private int maximumPoolSize = 10;
        private int idleTimeout = 30000;
    }
}
