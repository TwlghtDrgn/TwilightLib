package net.twlghtdrgn.twilightlib;

import net.twlghtdrgn.twilightlib.api.redis.RedisConnector;
import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;

/**
 * Loads this library as a plugin.
 */
public class TwilightLib extends TwilightPlugin {
    @Override
    protected void enable() throws ConfigurateException {
        Bukkit.getPluginManager().registerEvents(new ChatCallbackEvent(), this);
        RedisConnector.initJedis(this);
    }

    @Override
    protected void disable() throws IOException {
        RedisConnector.shutdown();
    }

    @Override
    public boolean reload() {
        return false;
    }
}
