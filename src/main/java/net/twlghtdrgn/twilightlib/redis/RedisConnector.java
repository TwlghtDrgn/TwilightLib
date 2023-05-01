package net.twlghtdrgn.twilightlib.redis;

import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.exception.ConfigLoadException;
import net.twlghtdrgn.twilightlib.TwilightLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.time.Duration;

/**
 * A Redis messaging connector using Jedis
 */
public class RedisConnector {
    private final JedisPool jedisPool;
    public RedisConnector() throws ConfigLoadException, IOException {
        FileConfiguration redis = ConfigLoader.legacy("redis.yaml");
        String host;
        String user;
        String password;
        int port;

        host = redis.getString("redis.hostname");
        port = redis.getInt("redis.port");
        user = redis.getString("redis.user");
        password = redis.getString("redis.password");

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
        TwilightLib.getPlugin().getLogger().info("Loaded Redis driver");
    }

    /**
     * Get a connection to Redis
     * @return database connection
     */
    @Nullable
    public Jedis getResource() {
        return jedisPool.getResource();
    }
}
