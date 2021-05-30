package fun.reallyisnt.oms.bungeecore.server.lobby;

import fun.reallyisnt.oms.bungeecore.server.ServerManager;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LobbyRedirectHandler implements ReconnectHandler {

    @Override
    public ServerInfo getServer(ProxiedPlayer player) {
        return ServerManager.LOBBY_REDIRECT;
    }

    @Override
    public void setServer(ProxiedPlayer player) {

    }

    @Override
    public void save() {

    }

    @Override
    public void close() {

    }
}
