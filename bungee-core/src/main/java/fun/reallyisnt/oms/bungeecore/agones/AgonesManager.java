package fun.reallyisnt.oms.bungeecore.agones;

import fun.reallyisnt.oms.AgonesSDK;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * This current implementation assumes that servers
 * must be alive for at least 5 minutes.
 */
public class AgonesManager implements Listener {

    // How long a server should wait before it is allowed to be shutdown in milliseconds
    private final static long SHUTDOWN_THRESHOLD = 300000;

    private final AgonesSDK agonesSDK = new AgonesSDK();
    private final long startUpTime = System.currentTimeMillis();

    public AgonesManager(Plugin plugin) {
        agonesSDK.reserve(SHUTDOWN_THRESHOLD / 1000);

        // Schedule health ping every 2.5 seconds
        ProxyServer.getInstance().getScheduler().schedule(plugin, agonesSDK::health, 0L, 2500L, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    public void onLogin(PreLoginEvent loginEvent) {
        agonesSDK.allocate();
        agonesSDK.alpha().playerConnect(loginEvent.getConnection().getUniqueId().toString());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        agonesSDK.alpha().playerDisconnect(event.getPlayer().getUniqueId().toString());
        // This is needed since BungeeCord first calls this event and then removes the player from the list
        // https://github.com/SpigotMC/BungeeCord/blob/master/proxy/src/main/java/net/md_5/bungee/connection/UpstreamBridge.java#L63
        if(ProxyServer.getInstance().getPlayers().size()-1 <= 0) {
            // If no players are on the server, return to ready status
            long timeSinceStart = System.currentTimeMillis() - startUpTime;
            if(timeSinceStart < SHUTDOWN_THRESHOLD) {
                // Reserve for the amount of time left till allowed to shutdown
                agonesSDK.reserve((SHUTDOWN_THRESHOLD - timeSinceStart) / 1000);
            }
            agonesSDK.ready();
        }
    }
}
