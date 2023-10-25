package net.twlghtdrgn.twilightlib;

import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import org.bukkit.Bukkit;

/**
 * Loads this library as a plugin.
 */
public class TwilightLib extends TwilightPlugin {
    @Override
    protected void enable() {
        Bukkit.getPluginManager().registerEvents(new ChatCallbackEvent(), this);
    }

    @Override
    protected void disable() {
        // also not needed
    }

    @Override
    public boolean reload() {
        return false;
    }
}
