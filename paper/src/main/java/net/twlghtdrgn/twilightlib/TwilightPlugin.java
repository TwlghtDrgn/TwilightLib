package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.LibraryInfo;
import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfo;
import net.twlghtdrgn.twilightlib.exception.PluginLoadException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

/**
 * An abstract library class
 */
@Getter
public abstract class TwilightPlugin extends JavaPlugin implements ILibrary {
    private TwilightPlugin plugin;
    private ConfigLoader configLoader;
    private PluginInfo pluginInfo;

    @Override
    public Path getPath() {
        return plugin.getDataFolder().toPath();
    }

    @Override
    public void onEnable() {
        plugin = this;
        pluginInfo = new PluginInfo(LibraryInfo.NAME,
                LibraryInfo.VERSION,
                getServer().getVersion(),
                LibraryInfo.URL);
        configLoader = new ConfigLoader(this);

        getLogger().info(pluginInfo.getStartupMessage());

        try {
            enable();
        } catch (PluginLoadException e) {
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
    protected abstract void enable() throws PluginLoadException;

    /**
     * Execute code on shutdown
     */
    protected abstract void disable();
}
