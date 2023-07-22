package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.util.PluginInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/**
 * An abstract library class
 */
@Getter
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    @Getter
    public static TwilightPlugin plugin;
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
        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    /**
     * Execute code on startup
     */
    protected abstract void enable();

    /**
     * Execute code on shutdown
     */
    protected abstract void disable();
}
