package fun.reallyisnt.oms.core.modules.dms.listeners;

import fun.reallyisnt.oms.core.modules.dms.redis.RedisUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void LeaveHandler(PlayerQuitEvent e) {
        RedisUtils.unsubscribeToPlayerChannel(e.getPlayer());
    }
}
