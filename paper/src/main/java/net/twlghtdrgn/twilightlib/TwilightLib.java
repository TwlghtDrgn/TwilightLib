package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.redis.RedisConnector;
import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import net.twlghtdrgn.twilightlib.packet.Minify;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;

/**
 * Loads this library as a plugin.
 */
public class TwilightLib extends TwilightPlugin {
    @Getter
    private static TwilightLib plugin;
    @Override
    protected void enable() throws ConfigurateException {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new ChatCallbackEvent(), this);
        RedisConnector.initJedis(this);
        new Minify(this);
    }

    @Override
    protected void disable() throws IOException {
        RedisConnector.close();
    }

    @Override
    public boolean reload() {
        return false;
    }
}
