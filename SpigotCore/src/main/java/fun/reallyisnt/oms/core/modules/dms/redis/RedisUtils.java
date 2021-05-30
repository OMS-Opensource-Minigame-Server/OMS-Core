package fun.reallyisnt.oms.core.modules.dms.redis;

import fun.reallyisnt.oms.core.OMSCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class RedisUtils {

    private static final RedisListener listener = new RedisListener();

    public static void subscribeToPlayerChannel(Player player) {
        BukkitScheduler scheduler = OMSCore.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(OMSCore.getInstance(), ()->OMSCore.getInstance().getJedis().subscribe(listener,"notifications:"+player.getUniqueId()));
    }

    public static void unsubscribeToPlayerChannel(Player player) {
        BukkitScheduler scheduler = OMSCore.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(OMSCore.getInstance(),
                ()->listener.unsubscribe("notifications:"+player.getUniqueId()));
    }

    public static void sendMessagetoChannel(UUID uuid, String message) {

    }
}
