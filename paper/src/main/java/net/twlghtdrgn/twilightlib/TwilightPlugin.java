package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfoProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * An abstract library class
 * Extend it if you are using Paper
 * For Velocity you should use "TwilightLib-API"
 */
@Getter
@SuppressWarnings("UnstableApiUsage")
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    private ConfigLoader configLoader;
    private PluginInfoProvider pluginInfo;

    /**
     * An SLF4J logger
     * @return {@link Logger}
     */
    @Override
    public Logger log() {
        return this.getSLF4JLogger();
    }

    /**
     * A plugin directory
     */
    @Override
    public Path getPath() {
        return getDataFolder().toPath();
    }


    /**
     * !!DO NOT OVERRIDE IT!!
     */
    @Override
    public void onEnable() {
        pluginInfo = new PluginInfoProvider(getPluginMeta().getName(),
                getPluginMeta().getVersion(),
                getServer().getVersion(),
                getPluginMeta().getWebsite());
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
     * !!DO NOT OVERRIDE IT!!
     */
    @Override
    public void onDisable() {
        disable();
    }

    /**
     * Code that executed on startup
     */
    protected abstract void enable() throws Exception;

    /**
     * Code that executed on shutdown
     */
    protected abstract void disable();
}
