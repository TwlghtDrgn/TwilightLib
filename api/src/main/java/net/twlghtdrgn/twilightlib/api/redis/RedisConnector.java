package net.twlghtdrgn.twilightlib.api.redis;

import lombok.Data;
import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.AbstractConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
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
    private final ILibrary library;
    public RedisConnector(@NotNull ILibrary library) {
        this.library = library;
        RedisConfig config = new RedisConfig("redis.yml", null);
        try {
            config.reload();
        } catch (IOException e) {
            throw new NullPointerException("Unable to load redis config");
        }

        String host = config.getConfig().getHostname();
        String user = config.getConfig().getUser();
        String password = config.getConfig().getPassword();
        int port = config.getConfig().getPort();

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
    }

    /**
     * Get a connection to Redis
     * @return database connection
     */
    @Nullable
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    @Getter
    protected class RedisConfig extends AbstractConfig {
        private Config config;
        protected RedisConfig(String configName, Class<?> configClass) {
            super(configName, Config.class);
        }

        @Override
        public void reload() throws ConfigurateException {
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
