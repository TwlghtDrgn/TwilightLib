package net.twlghtdrgn.twilightlib.redis;

import lombok.Data;
import net.twlghtdrgn.twilightlib.ILibrary;
import net.twlghtdrgn.twilightlib.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.time.Duration;

/**
 * A Redis messaging connector using Jedis
 */
@SuppressWarnings("unused")
public class RedisConnector {
    private final JedisPool jedisPool;
    private static ILibrary library;
    public RedisConnector(@NotNull ILibrary library) {
        this.library = library;
        RedisConfig config = new RedisConfig("redis.yml", null);
        String host = config.config.hostname;
        String user = config.config.user;
        String password = config.config.password;
        int port = config.config.port;

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setSoftMinEvictableIdleTime(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(60));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        jedisPool = new JedisPool(poolConfig,host,port,user,password);
        library.getLogger().info("Loaded Redis driver");
    }

    /**
     * Get a connection to Redis
     * @return database connection
     */
    @Nullable
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    protected static class RedisConfig extends AbstractConfig {
        private Config config;
        protected RedisConfig(String configName, Class<?> configClass) {
            super(configName, Config.class);
        }

        @Override
        public void reload() throws IOException {
            config = (Config) library.getConfigLoader().load(this);
        }

        @Data
        @ConfigSerializable
        public static class Config {
            private String hostname = "127.0.0.1";
            private String user = "admin";
            private String password = "password";
            private int port;
        }
    }
}
