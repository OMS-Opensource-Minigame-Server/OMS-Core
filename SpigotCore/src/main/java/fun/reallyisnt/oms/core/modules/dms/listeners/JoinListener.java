package fun.reallyisnt.oms.core.modules.dms.listeners;

import fun.reallyisnt.oms.core.modules.dms.redis.RedisUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void JoinEventListener(PlayerJoinEvent e) {
        RedisUtils.subscribeToPlayerChannel(e.getPlayer());
    }
}
