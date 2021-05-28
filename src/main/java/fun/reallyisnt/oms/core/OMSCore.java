package fun.reallyisnt.oms.core;

import fun.reallyisnt.oms.core.modules.Module;
import fun.reallyisnt.oms.core.modules.Modules;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class OMSCore extends JavaPlugin {

    private static OMSCore instance;
    private Map<String, Module> modules;
    private Jedis jedis;


    public String getConfigString(Config node) {
        String envStr = System.getenv(node.name());
        if (envStr != null) {
            return envStr;
        } else {
            return this.getConfig().getString(node.path);
        }
    }

    @Override
    public void onEnable() {
        OMSCore.instance = this;
        this.saveDefaultConfig();

        //Load Redis
        if (this.getConfigString(Config.REDIS_ENABLED).equalsIgnoreCase("true")) {
            this.jedis = new Jedis(this.getConfigString(Config.REDIS_HOST),Integer.parseInt(this.getConfigString(Config.REDIS_PASSWORD)));
        }


        //Initialize all Modules
        this.modules = new HashMap<>();
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
        if (this.jedis != null) {
            this.jedis.close();
        }


    }

    public Jedis getJedis() {
        return this.jedis;
    }

    public static OMSCore getInstance() {
        return instance;
    }
}
