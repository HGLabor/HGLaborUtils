package de.hglabor.utils.noriskutils;

import redis.clients.jedis.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class JedisUtils {
    public static final String GLOBALCHAT_ENABLE_CHANNEL = "globalchat-enable";
    public static final String GLOBALCHAT_DISABLE_CHANNEL = "globalchat-disable";
    public static final String CUSTOMCHAT_ENABLE_CHANNEL = "customchat-enable";
    public static final String CUSTOMCHAT_DISABLE_CHANNEL = "customchat-disable";
    private static JedisPool jedisPool;

    private JedisUtils() {
    }

    public static void init(String password) {
        jedisPool = new JedisPool(buildPoolConfig(), Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, password);
    }

    private static JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    public static void subscribe(JedisPubSub channel, String... channels) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(channel, channels);
            }
        });
    }

    public static void publish(String channel, String message) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, message);
            }
        });
    }

    public static void closePool() {
        jedisPool.close();
    }
}
