package net.twlghtdrgn.twilightlib;

import org.bukkit.plugin.java.JavaPlugin;

public final class TwilightLib extends JavaPlugin {
    private static JavaPlugin plugin;

    /**
     * Allows you to get your own plugin. Useful only inside my library
     * @return {@link JavaPlugin} for internal classes.
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Set your plugin instance to use library
     * @param plugin your plugin
     */
    public static void setPlugin(final JavaPlugin plugin) {
        TwilightLib.plugin = plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("\n" +
                "|| TwlghtDrgn's TwilghtLib\n" +
                "||\n" +
                "|| Version: " + getPluginMeta().getVersion() + "\n" +
                "|| Server version: " + getServer().getVersion() + "\n" +
                "|| GitHub: " + getPluginMeta().getWebsite() + "\n" +
                "|| Discord support (#plugin-support): https://discord.gg/PddWSCDBhP");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}