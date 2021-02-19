package de.hglabor.utils.noriskutils.jedis;

import redis.clients.jedis.*;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public final class JedisUtils {
    private static JedisPool jedisPool;

    private JedisUtils() {
    }

    public static void init(String password) {
        jedisPool = new JedisPool(JedisUtils.buildPoolConfig(), Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, password);
    }

    public static JedisPoolConfig buildPoolConfig() {
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
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, message);
        }
    }

    public static void closePool() {
        jedisPool.close();
    }
}
