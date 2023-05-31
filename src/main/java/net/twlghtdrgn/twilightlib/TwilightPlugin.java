package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An abstract library class
 */
@Getter
public abstract class TwilightPlugin extends JavaPlugin {
    private static TwilightPlugin plugin;
    private Config configuration;

    @Override
    public void onEnable() {
        plugin = this;
        configuration = new Config(this);
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
        getLogger().info("\n" +
                "|| Disabling " + getName() + " ver" + getPluginMeta().getVersion());
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
