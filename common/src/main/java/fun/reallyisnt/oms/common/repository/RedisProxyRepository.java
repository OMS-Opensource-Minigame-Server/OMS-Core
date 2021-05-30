package fun.reallyisnt.oms.common.repository;

import fun.reallyisnt.oms.common.models.ProxyServer;
import redis.clients.jedis.*;

import java.util.*;
import java.util.stream.Collectors;

public class RedisProxyRepository {

    private static final String REPOSITORY_KEY = "PROXIES:";
    public static final long TIMEOUT = 5000;

    private final JedisPool jedisPool;

    public RedisProxyRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public List<ProxyServer> getProxyServers() {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = Long.parseLong(jedis.time().get(0));
            Set<String> names = jedis.zrangeByScore(REPOSITORY_KEY + "proxies", "(" + currentTime, "+inf");

            Pipeline pipeline = jedis.pipelined();
            Set<Response<Map<String, String>>> set = new HashSet<>();
            for (String name : names) {
                set.add(pipeline.hgetAll(REPOSITORY_KEY + name));
            }

            pipeline.sync();

            return set.stream()
                    .map(Response::get)
                    .filter(Objects::nonNull)
                    .map(ProxyServer::deserialize)
                    .collect(Collectors.toList());
        }
    }

    public void registerProxy(ProxyServer proxyServer) {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = Long.parseLong(jedis.time().get(0));
            long seconds = TIMEOUT / 1000;

            Transaction transaction = jedis.multi();
            transaction.zadd(REPOSITORY_KEY + "proxies", currentTime + seconds, proxyServer.getName());
            transaction.hset(REPOSITORY_KEY + proxyServer.getName(), proxyServer.serialize());
            transaction.expire(REPOSITORY_KEY + proxyServer.getName(), seconds);
            transaction.exec();
        }
    }

    public void removeProxy(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            Transaction transaction = jedis.multi();
            transaction.del(REPOSITORY_KEY + name);
            transaction.zrem(REPOSITORY_KEY + "proxies", name);
            transaction.exec();
        }
    }
}
