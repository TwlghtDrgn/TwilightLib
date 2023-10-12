package net.twlghtdrgn.twilightlib.api.redis;

import lombok.Data;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.time.Duration;

/**
 * A Redis messaging connector using Jedis
 * @author TwlghtDrgn
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class RedisConnector {
    private final JedisPool jedisPool;
    private final ILibrary library;
    private final Configuration<RedisConfig> redisConfig;
    public RedisConnector(@NotNull ILibrary library) throws ConfigurateException, IllegalStateException {
        this.library = library;
        redisConfig = new Configuration<>(library, RedisConfig.class, "redis");
        redisConfig.reload();

        String host = redisConfig.get().getHostname();
        String user = redisConfig.get().getUser();
        String password = redisConfig.get().getPassword();
        int port = redisConfig.get().getPort();

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

        jedisPool = new JedisPool(poolConfig, host, port, user, password);
        library.log().info("Testing Redis connection...");
        try (Jedis jedis = getResource()) {
            library.log().info("Redis connection: OK");
        } catch (JedisConnectionException e) {
            library.log().error("Unable to connect. Is credentials are wrong?", e);
            throw new IllegalStateException("Redis cannot be loaded");
        }
    }

    /**
     * Get a connection to Redis
     * @return database connection
     */
    @Nullable
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public void close() throws IOException {
        library.log().info("Shutting down Redis");
        this.jedisPool.close();
    }

    @Data
    @ConfigSerializable
    protected static class RedisConfig {
        private String hostname = "127.0.0.1";
        private String user = "redis";
        private String password = "password";
        private int port = 6379;
    }
}
