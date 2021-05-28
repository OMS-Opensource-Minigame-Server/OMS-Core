package fun.reallyisnt.oms.core.modules;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import com.google.common.base.Charsets;
import fun.reallyisnt.oms.core.OMSCore;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Module implements Plugin {

    private File dataFolder = null;
    private File configFile = null;
    Logger logger = null;
    private boolean isEnabled = false;
    private PluginLoader loader = null;
    private Server server = null;
    private PluginDescriptionFile description = null;
    private boolean naggable = true;
    private FileConfiguration config = null;
    private String name;

    public Module(String name) {
        this.name = name;
        this.loader = OMSCore.getInstance().getPluginLoader();
        this.server = OMSCore.getInstance().getServer();
        this.description = OMSCore.getInstance().getDescription();
        this.dataFolder = new File(OMSCore.getInstance().getDataFolder(), name);
        this.configFile = new File(dataFolder, "config.yml");
        if (this.logger == null) {
            this.logger = PaperPluginLogger.getLogger(this.description);
        }
    }

    //A lot of this code was taken from Paper's implementation of the Bukkit API

    @Override
    public @NotNull File getDataFolder() {
        return new File(OMSCore.getInstance().getDataFolder(),this.name);
    }

    @Override
    public @NotNull PluginDescriptionFile getDescription() {
        return OMSCore.getInstance().getDescription();
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }

        return this.config;
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String s) {
        return OMSCore.getInstance().getResource(s);
    }

    @Override
    public void saveConfig() {
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException var2) {
            this.logger.log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (!resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            String originalPath = resourcePath;
            resourcePath = "modules/"+name+"/"+resourcePath;
            InputStream in = this.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in OMSCore");
            } else {
                File outFile = new File(this.dataFolder, originalPath);
                int lastIndex = originalPath.lastIndexOf(47);
                File outDir = new File(this.dataFolder, originalPath.substring(0, Math.max(lastIndex, 0)));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        this.logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = new FileOutputStream(outFile);
                        byte[] buf = new byte[1024];

                        int len;
                        while((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException e) {
                    this.logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, e);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    @Override
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.getResource("config.yml");
        if (defConfigStream != null) {
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }
    }

    @Override
    public @NotNull PluginLoader getPluginLoader() {
        return OMSCore.getInstance().getPluginLoader();
    }

    @Override
    public @NotNull Server getServer() {
        return OMSCore.getInstance().getServer();
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public boolean isNaggable() {
        return this.naggable;
    }

    @Override
    public void setNaggable(boolean b) {
        this.naggable = b;
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String s, @Nullable String s1) {
        return null;
    }

    @Override
    public @NotNull Logger getLogger() {
        return OMSCore.getInstance().getLogger();
    }

    @Override
    public @NotNull org.slf4j.@NotNull Logger getSLF4JLogger() {
        return OMSCore.getInstance().getSLF4JLogger();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setEnabled(boolean enabled) {
        if (this.isEnabled != enabled) {
            this.isEnabled = enabled;
            if (this.isEnabled) {
                this.onEnable();
            } else {
                this.onDisable();
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
