package fun.reallyisnt.oms.bungeecore.server.lobby;

import fun.reallyisnt.oms.common.models.ServerData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.atomic.AtomicInteger;

public class LobbyWrapper {

    private final ServerData server;
    private final AtomicInteger additionalPlayers = new AtomicInteger();

    public LobbyWrapper(ServerData server) {
        this.server = server;
    }

    public String getName() {
        return server.name();
    }

    public int getPlayerCount() {
        return server.playerCount();
    }

    public int getMaxPlayerCount() {
        return server.maxPlayerCount();
    }

    public void increment() {
        additionalPlayers.incrementAndGet();
    }

    public void decrement() {
        additionalPlayers.decrementAndGet();
    }

    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(getName());
    }
}
