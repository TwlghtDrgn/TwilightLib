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
 */
@Getter
@SuppressWarnings("UnstableApiUsage")
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    private ConfigLoader configLoader;
    private PluginInfoProvider pluginInfo;

    @Override
    public Logger log() {
        return this.getSLF4JLogger();
    }

    @Override
    public Path getPath() {
        return getDataFolder().toPath();
    }

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
            log().error("{} cannot be loaded properly and will be disabled. You can find a stacktrace below", getName());
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
