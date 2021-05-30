package fun.reallyisnt.oms.bungeecore.player;

import fun.reallyisnt.oms.bungeecore.proxy.ProxyManager;
import fun.reallyisnt.oms.common.models.OnlinePlayer;
import fun.reallyisnt.oms.common.repository.RedisPlayerRepository;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerManager implements Runnable {

    private final RedisPlayerRepository repository;
    private final ProxyManager proxyManager;

    public PlayerManager(Plugin plugin, ProxyManager proxyManager, JedisPool jedisPool) {
        this.proxyManager = proxyManager;
        this.repository = new RedisPlayerRepository(jedisPool);

        plugin.getProxy().getScheduler().schedule(plugin, this, 2500L, 2500L, TimeUnit.MILLISECONDS);
        plugin.getProxy().getPluginManager().registerListener(plugin, new PlayerListener(this.repository));
    }

    @Override
    public void run() {
        List<OnlinePlayer> players = ProxyServer.getInstance().getPlayers()
                .stream()
                .map(player -> new OnlinePlayer(player.getDisplayName(), player.getUniqueId(), (player.getServer() == null ? "Unknown" : player.getServer().getInfo().getName()), proxyManager.getProxyName()))
                .collect(Collectors.toList());

        this.repository.registerPlayers(players);
    }
}
