package fun.reallyisnt.oms.bungeecore.player;

import fun.reallyisnt.oms.common.repository.RedisPlayerRepository;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    private final RedisPlayerRepository repository;

    public PlayerListener(RedisPlayerRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        this.repository.removePlayer(e.getPlayer().getName(), e.getPlayer().getUniqueId());
    }
}
