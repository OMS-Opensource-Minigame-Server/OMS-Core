package fun.reallyisnt.oms.core;

import fun.reallyisnt.oms.core.modules.Module;
import fun.reallyisnt.oms.core.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class OMSCore extends JavaPlugin {

    private static OMSCore instance;
    private Map<String, Module> modules;


    @Override
    public void onEnable() {
        OMSCore.instance = this;
        this.modules = new HashMap<>();

        //Initialize all Modules
        for (Modules m : Modules.values()) {
            try {
                this.getLogger().info("Starting module "+m.name());
                Module module = m.getClazz().newInstance();
                module.setEnabled(this.getConfig().getBoolean("modules."+module.getName()));
                modules.put(module.getName(),module);
            } catch (Throwable e) {
                this.getLogger().log(Level.SEVERE, "Error loading module "+m.name(), e);
            }
        }
    }

    @Override
    public void onDisable() {



    }

    public static OMSCore getInstance() {
        return instance;
    }
}
