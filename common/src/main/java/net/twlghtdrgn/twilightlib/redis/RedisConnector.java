package net.twlghtdrgn.twilightlib.redis;

import lombok.Data;
import lombok.Getter;
import net.twlghtdrgn.twilightlib.ILibrary;
import net.twlghtdrgn.twilightlib.config.Configuration;
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
    private static Configuration<RedisConfig> cfg;
    @Getter
    private static boolean enabled;
    public static void initJedis(ILibrary lib) throws ConfigurateException, IllegalStateException {
        library = lib;

        cfg = new Configuration<>(library, RedisConfig.class, "redis");
        cfg.reload();
        if (!cfg.get().isJedisEnabled()) {
            library.log().info("Redis is disabled in the library configuration, skipping..");
            return;
        } else library.log().info("Redis is enabled in the library configuration, loading..");

        String host = cfg.get().getHostname();
        String user = cfg.get().getUser();
        String password = cfg.get().getPassword();
        int port = cfg.get().getPort();

        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMinIdle(cfg.get().getAdvanced().getMinIdle());
        poolConfig.setMaxIdle(cfg.get().getAdvanced().getMaxIdle());
        poolConfig.setMaxTotal(cfg.get().getAdvanced().getMaxTotal());

        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        poolConfig.setSoftMinEvictableIdleDuration(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(60));
        poolConfig.setNumTestsPerEvictionRun(3);

        poolConfig.setBlockWhenExhausted(true);

        if (user.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port);
        } else if (password.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port, user, null);
        } else jedisPool = new JedisPool(poolConfig, host, port, user, password);

        library.log().info("Testing Redis connection...");
        try (Jedis jedis = getResource()) {
            library.log().info("Redis connection: OK");
            enabled = true;
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
        return jedisPool != null ? jedisPool.getResource() : null;
    }

    /**
     * Used to send a Redis messages
     * @param channel a channel of the message
     * @param data a message data
     * @return true if message was sent successfully
     */
    public static boolean sendMessage(final @NotNull String channel, final byte[] data) {
        try (Jedis jedis = getResource()) {
            if (!enabled || jedis == null) return false;
            jedis.publish(channel.getBytes(), data);
            return true;
        } catch (Exception e) {
            library.log().error("An error has occurred when a plugin tried to publish a message", e);
            return false;
        }
    }

    /**
     * Shuts down a Jedis pool
     */
    public static void shutdown() throws IOException {
        if (!cfg.get().isJedisEnabled()) return;
        library.log().info("Shutting down Redis");
        if (jedisPool != null) jedisPool.close();
    }

    /**
     * A jedis configuration file
     */
    @Data
    @ConfigSerializable
    protected static class RedisConfig {
        private boolean jedisEnabled = false;
        private String hostname = "127.0.0.1";
        private String user = "";
        private String password = "";
        private int port = 6379;

        private final Advanced advanced = new Advanced();

        @Data
        @ConfigSerializable
        public static class Advanced {
            private int minIdle = 16;
            private int maxIdle = 128;
            private int maxTotal = 128;
        }
    }
}
