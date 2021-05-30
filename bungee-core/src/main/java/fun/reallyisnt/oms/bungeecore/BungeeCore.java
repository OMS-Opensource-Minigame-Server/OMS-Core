package fun.reallyisnt.oms.bungeecore;

import fun.reallyisnt.oms.bungeecore.agones.AgonesManager;
import fun.reallyisnt.oms.bungeecore.player.PlayerManager;
import fun.reallyisnt.oms.bungeecore.proxy.ProxyManager;
import fun.reallyisnt.oms.common.Config;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BungeeCore extends Plugin {

    private Configuration config;
    private JedisPool jedisPool;

    @Override
    public void onEnable() {
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            try {
                File file = new File(getDataFolder(), "config.yml");
                if(!file.exists()) {
                    file.createNewFile();
                }

                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                this.getLogger().log(Level.SEVERE, "Failed to load config", e);
            }
        }

        if (getConfigString(Config.REDIS_ENABLED, "true").equalsIgnoreCase("true")) {
            this.jedisPool = new JedisPool(new GenericObjectPoolConfig<>(), getConfigString(Config.REDIS_HOST), Integer.parseInt(getConfigString(Config.REDIS_PORT)), 100, getConfigString(Config.REDIS_PASSWORD));
            ProxyManager proxyManager = new ProxyManager(this, jedisPool);
            new PlayerManager(this, proxyManager, jedisPool);
        }

        if (getConfigString(Config.AGONES_ENABLED, "true").equalsIgnoreCase("true")) {
            getProxy().getPluginManager().registerListener(this, new AgonesManager(this));
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (getConfigString(Config.REDIS_ENABLED).equalsIgnoreCase("true")) {
            this.jedisPool.close();
        }
    }

    public String getConfigString(Config node, String defaultValue) {
        String value = getConfigString(node);
        return (value == null || value.isEmpty()) ? defaultValue : value;
    }

    public String getConfigString(Config node) {
        String envStr = System.getenv(node.name());
        if (envStr != null) {
            return envStr;
        } else {
            return config.getString(node.getPath());
        }
    }
}
