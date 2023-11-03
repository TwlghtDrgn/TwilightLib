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
    private RedisConnector() {}
    private static JedisPool jedisPool;
    private static ILibrary library;
    private static Configuration<RedisConfig> redisConfig;
    public static void initJedis(ILibrary lib) throws ConfigurateException, IllegalStateException {
        library = lib;

        redisConfig = new Configuration<>(library, RedisConfig.class, "redis");
        redisConfig.reload();
        if (!redisConfig.get().isJedisEnabled()) {
            library.log().info("Redis is disabled in the library configuration, skipping..");
            return;
        } else library.log().info("Redis is enabled in the library configuration, loading..");

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

        if (user.length() == 0) {
            jedisPool = new JedisPool(poolConfig, host, port);
        } else if (password.length() == 0) {
            jedisPool = new JedisPool(poolConfig, host, port, user, null);
        } else jedisPool = new JedisPool(poolConfig, host, port, user, password);

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
    public static Jedis getResource() {
        return jedisPool.getResource();
    }

    public static boolean sendMessage(final @NotNull String channel, final byte[] data) {
        try (Jedis jedis = getResource()) {
            jedis.publish(channel.getBytes(), data);
            return true;
        } catch (Exception e) {
            library.log().error("An error has occurred when a plugin tried to publish a message", e);
            return false;
        }
    }

    public static void close() throws IOException {
        if (!redisConfig.get().isJedisEnabled()) return;
        library.log().info("Shutting down Redis");
        jedisPool.close();
    }

    @Data
    @ConfigSerializable
    protected static class RedisConfig {
        private boolean jedisEnabled = false;
        private String hostname = "127.0.0.1";
        private String user = "";
        private String password = "";
        private int port = 6379;
    }
}
