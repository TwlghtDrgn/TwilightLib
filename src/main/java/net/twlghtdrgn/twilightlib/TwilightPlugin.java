package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An abstract library class
 */
@Getter
public abstract class TwilightPlugin extends JavaPlugin {
    private static TwilightPlugin plugin;
    private ConfigLoader configLoader;

    @Override
    public void onEnable() {
        plugin = this;
        configLoader = new ConfigLoader(this);
        getLogger().info("\n" +
                "|| " + getName() + "\n" +
                "||\n" +
                "|| Version: " + getPluginMeta().getVersion() + "\n" +
                "|| Server version: " + getServer().getVersion() + "\n" +
                "|| GitHub: " + getPluginMeta().getWebsite() + "\n" +
                "|| Discord support (#plugin-support): https://discord.gg/PddWSCDBhP");
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
