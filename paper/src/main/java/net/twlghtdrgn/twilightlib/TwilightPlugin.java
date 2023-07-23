package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.util.PluginInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/**
 * An abstract library class
 */
@Getter
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    @Getter
    private static TwilightPlugin plugin;
    private ConfigLoader configLoader;
    private PluginInfo pluginInfo;

    @Override
    public Path getPath() {
        return plugin.getDataFolder().toPath();
    }

    @Override
    public void onEnable() {
        plugin = this;
        pluginInfo = new PluginInfo(getPluginMeta().getName(),
                getPluginMeta().getVersion(),
                getServer().getVersion(),
                getPluginMeta().getWebsite());
        configLoader = new ConfigLoader(this);

        getLogger().info(pluginInfo.getStartupMessage());

        try {
            enable();
        } catch (Exception e) {
            getLogger().severe(getName() + " cannot be loaded properly and will be disabled. You can find a stacktrace below");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        disable();
    }

    /**
     * Execute code on startup
     */
    protected abstract void enable() throws Exception;

    /**
     * Execute code on shutdown
     */
    protected abstract void disable();
}
