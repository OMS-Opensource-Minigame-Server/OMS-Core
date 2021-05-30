package fun.reallyisnt.oms.bungeecore.server;

import fun.reallyisnt.oms.bungeecore.server.lobby.LobbyRedirectHandler;
import fun.reallyisnt.oms.bungeecore.server.lobby.LobbySorter;
import fun.reallyisnt.oms.bungeecore.server.lobby.LobbyWrapper;
import fun.reallyisnt.oms.common.models.ServerData;
import fun.reallyisnt.oms.common.repository.RedisServerRepository;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.JedisPool;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerManager implements Runnable, Listener {

    private static final LobbySorter LOBBY_SORTER = new LobbySorter();
    public static final ServerInfo LOBBY_REDIRECT = ProxyServer.getInstance().constructServerInfo("Lobby", new InetSocketAddress(1235), "", false);

    private final RedisServerRepository repository;
    private final Plugin plugin;

    private List<LobbyWrapper> lobbies;

    public ServerManager(Plugin plugin, JedisPool jedisPool) {
        this.plugin = plugin;
        this.repository = new RedisServerRepository(jedisPool);

        plugin.getProxy().setReconnectHandler(new LobbyRedirectHandler());
        plugin.getProxy().getServers().clear();
        plugin.getProxy().getServers().put("Lobby", LOBBY_REDIRECT);

        plugin.getProxy().getScheduler().schedule(plugin, this, 2500L, 2500L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        Map<String, ServerInfo> map = plugin.getProxy().getServers();
        Map<String, ServerData> serverDataMap = repository.getServers().stream().collect(Collectors.toMap(ServerData::getName, o -> o));

        lobbies = serverDataMap.values().stream().map(LobbyWrapper::new).collect(Collectors.toList());
        for (ServerData server : serverDataMap.values()) {
            if (!map.containsKey(server.getName())) {
                ServerInfo info = plugin.getProxy().constructServerInfo(server.getName(), new InetSocketAddress(server.getIpAddress(), 25565), "", false);
                map.put(server.getName(), info);
            }
        }

        for (ServerInfo info : map.values()) {
            if (!serverDataMap.containsKey(info.getName()) && !info.equals(LOBBY_REDIRECT)) {
                map.remove(info.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerConnect(ServerConnectEvent event) {
        if (!event.getTarget().equals(LOBBY_REDIRECT)) {
            return;
        }

        Optional<LobbyWrapper> wrapperOptional = lobbies.stream().min(LOBBY_SORTER);
        if(!wrapperOptional.isPresent()) {
            event.getPlayer().disconnect(TextComponent.fromLegacyText(ChatColor.RED + "Unable to find a lobby for you. Please try again later."));
            return;
        }

        event.setTarget(wrapperOptional.get().getServerInfo());
    }

    @EventHandler
    public void onPlayerConnect(ServerConnectedEvent event) {
        String name = event.getServer().getInfo().getName();
        for (LobbyWrapper lobby : lobbies) {
            if(lobby.getName().equals(name)) {
                lobby.increment();
            }
        }
    }

    @EventHandler
    public void onPlayerDisconnect(ServerDisconnectEvent event) {
        String name = event.getTarget().getName();
        for (LobbyWrapper lobby : lobbies) {
            if(lobby.getName().equals(name)) {
                lobby.decrement();
            }
        }
    }
}
