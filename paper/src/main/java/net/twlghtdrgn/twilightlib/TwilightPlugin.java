package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.util.PluginInfoProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * An abstract plugin class
 * @see JavaPlugin
 * @see ILibrary
 */
@Getter
@SuppressWarnings("UnstableApiUsage")
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    private ConfigLoader configLoader;
    private PluginInfoProvider pluginInfo;

    /**
     * @see ILibrary
     */
    @Override
    public Logger log() {
        return this.getSLF4JLogger();
    }

    /**
     * @see ILibrary
     */
    @Override
    public Path getPath() {
        return getDataFolder().toPath();
    }


    /**
     * Internal usage
     */
    @Override
    public void onEnable() {
        pluginInfo = new PluginInfoProvider(getPluginMeta().getName(),
                getPluginMeta().getVersion(),
                getServer().getVersion());
        configLoader = new ConfigLoader(this);

        log().info(pluginInfo.getStartupMessage());

        try {
            enable();
        } catch (Exception e) {
            log().error("{} cannot be loaded properly and will be disabled", getName(), e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Internal usage
     */
    @Override
    public void onDisable() {
        try {
            disable();
        } catch (Exception e) {
            log().error("An error occurred while unloading {}", getName(), e);
        }
    }

    /**
     * Code that executed on startup
     */
    protected abstract void enable() throws Exception;

    /**
     * Code that executed on shutdown
     */
    protected abstract void disable() throws Exception;
}
