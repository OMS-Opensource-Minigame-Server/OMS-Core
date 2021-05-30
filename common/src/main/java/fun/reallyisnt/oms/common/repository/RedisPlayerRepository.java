package fun.reallyisnt.oms.common.repository;

import fun.reallyisnt.oms.common.models.OnlinePlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RedisPlayerRepository {

    private final static String REPOSITORY_KEY = "PLAYERS:";
    public final static long TIMEOUT = 5000;

    private final JedisPool jedisPool;

    public RedisPlayerRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Optional<OnlinePlayer> searchPlayer(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(REPOSITORY_KEY + name);
            if (map == null || map.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(OnlinePlayer.deserialize(map));
        }
    }

    public Optional<OnlinePlayer> searchPlayer(UUID uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(REPOSITORY_KEY + uuid);

            return (map == null || map.isEmpty()) ? Optional.empty() : Optional.of(OnlinePlayer.deserialize(map));
        }
    }

    public void registerPlayers(Collection<OnlinePlayer> onlinePlayers) {
        try (Jedis jedis = jedisPool.getResource()) {
            Transaction transaction = jedis.multi();
            for (OnlinePlayer onlinePlayer : onlinePlayers) {
                transaction.hset(REPOSITORY_KEY + onlinePlayer.getName(), onlinePlayer.serialize());
                transaction.expire(REPOSITORY_KEY + onlinePlayer.getName(), TIMEOUT / 1000);
                transaction.hset(REPOSITORY_KEY + onlinePlayer.getUuid().toString(), onlinePlayer.serialize());
                transaction.expire(REPOSITORY_KEY + onlinePlayer.getUuid(), TIMEOUT / 1000);
            }

            transaction.exec();
        }
    }

    public void removePlayer(String name, UUID uuid) {
        try (Jedis jedis = jedisPool.getResource()) {
            Transaction transaction = jedis.multi();
            transaction.del(REPOSITORY_KEY + name);
            transaction.del(REPOSITORY_KEY + uuid.toString());
            transaction.exec();
        }
    }
}