package fun.reallyisnt.oms.bungeecore;

import fun.reallyisnt.oms.bungeecore.agones.AgonesModule;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCore extends Plugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("hi");

        //TODO: Add config option for this
        this.getProxy().getPluginManager().registerListener(this, new AgonesModule(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
