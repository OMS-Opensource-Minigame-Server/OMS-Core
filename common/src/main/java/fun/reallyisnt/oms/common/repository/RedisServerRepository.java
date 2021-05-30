package fun.reallyisnt.oms.common.repository;

import fun.reallyisnt.oms.common.models.ServerData;
import redis.clients.jedis.*;

import java.util.*;
import java.util.stream.Collectors;

public class    RedisServerRepository {

    private static final String REPOSITORY_KEY = "SERVERS:";
    public static final long TIMEOUT = 7500;

    private final JedisPool jedisPool;

    public RedisServerRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public List<ServerData> getServers() {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = Long.parseLong(jedis.time().get(0));
            Set<String> names = jedis.zrangeByScore(REPOSITORY_KEY + "servers", "(" + currentTime, "+inf");

            Pipeline pipeline = jedis.pipelined();
            Set<Response<Map<String, String>>> set = new HashSet<>();
            for (String name : names) {
                set.add(pipeline.hgetAll(REPOSITORY_KEY + name));
            }

            pipeline.sync();

            return set.stream()
                    .map(Response::get)
                    .filter(Objects::nonNull)
                    .map(ServerData::deserialize)
                    .collect(Collectors.toList());
        }
    }

    public Optional<ServerData> searchServer(String serverName) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(REPOSITORY_KEY + serverName);
            return (map == null || map.isEmpty()) ? Optional.empty() : Optional.of(ServerData.deserialize(map));
        }
    }

    public void registerServer(ServerData serverData) {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = Long.parseLong(jedis.time().get(0));
            long seconds = TIMEOUT / 1000;

            Transaction transaction = jedis.multi();
            transaction.zadd(REPOSITORY_KEY + "servers", currentTime + seconds, serverData.name());
            transaction.hset(REPOSITORY_KEY + serverData.name(), serverData.serialize());
            transaction.expire(REPOSITORY_KEY + serverData.name(), seconds);
            transaction.exec();
        }
    }
}
