package fun.reallyisnt.oms.bungeecore.proxy;

import fun.reallyisnt.oms.common.models.ProxyServer;
import fun.reallyisnt.oms.common.repository.RedisProxyRepository;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyManager implements Runnable, Listener {

    private final RedisProxyRepository repository;
    private final AtomicInteger playerCount = new AtomicInteger();

    public ProxyManager(Plugin plugin, JedisPool jedisPool) {
        this.repository = new RedisProxyRepository(jedisPool);

        plugin.getProxy().getPluginManager().registerListener(plugin, this);
        plugin.getProxy().getScheduler().schedule(plugin, this, 0L, 2000L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        repository.registerProxy(new ProxyServer(getProxyName(), net.md_5.bungee.api.ProxyServer.getInstance().getOnlineCount()));

        int totalPlayerCount = 0;
        for (ProxyServer proxyServer : repository.getProxyServers()) {
            totalPlayerCount += proxyServer.getPlayerCount();
        }

        playerCount.set(totalPlayerCount);
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        int cPlayerCount = playerCount.get();

        event.getResponse().getPlayers().setOnline(cPlayerCount);
        event.getResponse().getPlayers().setMax(cPlayerCount+1);
        event.getResponse().getModinfo().setType("VANILLA");
    }

    public String getProxyName() {
        return "";
    }
}
