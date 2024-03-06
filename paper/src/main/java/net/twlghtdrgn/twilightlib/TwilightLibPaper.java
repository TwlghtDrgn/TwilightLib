package net.twlghtdrgn.twilightlib;

import net.twlghtdrgn.twilightlib.redis.RedisConnector;
import net.twlghtdrgn.twilightlib.listener.CallbackListener;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;

/**
 * Loads this library as a plugin.
 */
public class TwilightLibPaper extends TwilightPlugin {
    @Override
    protected void enable() throws ConfigurateException {
        Bukkit.getPluginManager().registerEvents(new CallbackListener(), this);
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
