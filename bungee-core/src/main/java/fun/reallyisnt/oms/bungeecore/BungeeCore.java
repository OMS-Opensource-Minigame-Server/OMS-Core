package fun.reallyisnt.oms.bungeecore;

import fun.reallyisnt.oms.AgonesSDK;
import fun.reallyisnt.oms.bungeecore.agones.AgonesManager;
import fun.reallyisnt.oms.bungeecore.player.PlayerManager;
import fun.reallyisnt.oms.bungeecore.proxy.ProxyManager;
import fun.reallyisnt.oms.common.Config;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class BungeeCore extends Plugin {

    private Configuration config;
    private JedisPool jedisPool;

    @Override
    public void onEnable() {
        try {
            if(!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File file = new File(getDataFolder(), "config.yml");
            if(!file.exists()) {
                file.createNewFile();
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, "Failed to load config", e);
        }

        String proxyName = getConfigString(Config.SERVER_NAME);

        if (getConfigBoolean(Config.AGONES_ENABLED)) {
            AgonesSDK agonesSDK = new AgonesSDK();
            proxyName = agonesSDK.gameServer().getMeta().getLabels().get("osm.reallyisnt.fun/ordinalname");
            new AgonesManager(this, agonesSDK);
        }

        if (getConfigBoolean(Config.REDIS_ENABLED)) {
            this.jedisPool = new JedisPool(new GenericObjectPoolConfig<>(), getConfigString(Config.REDIS_HOST), Integer.parseInt(getConfigString(Config.REDIS_PORT)), 100, getConfigString(Config.REDIS_PASSWORD));
            ProxyManager proxyManager = new ProxyManager(this, proxyName, jedisPool);
            new PlayerManager(this, proxyManager, jedisPool);
        }
    }

    @Override
    public void onDisable() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    public String getConfigString(Config node) {
        String value = System.getenv(node.name());
        if (value == null && config.contains(node.getPath())) {
            return config.getString(node.getPath());
        }

        return (value == null || value.isEmpty()) ? node.getDefaultValue() : value;
    }

    public boolean getConfigBoolean(Config node) {
        return getConfigString(node).equalsIgnoreCase("true");
    }
}
