package net.twlghtdrgn.twilightlib;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.redis.RedisConnector;
import net.twlghtdrgn.twilightlib.util.PluginInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "twilightlib",
        name = "TwilightLib-Velocity",
        version = LibraryVersion.VERSION,
        authors = {"TwlghtDrgn"},
        url = "https://github.com/TwlghtDrgn/TwilightLib",
        dependencies = {@Dependency(id = "miniplaceholders", optional = true)}
)
public class TwilightLibVelocity implements ILibrary {
    @Getter
    private final PluginInfoProvider pluginInfo;
    private final Logger logger;
    private final Path path;

    @Inject
    public TwilightLibVelocity(@NotNull Logger logger, @NotNull ProxyServer server, @DataDirectory Path path) {
        pluginInfo = new PluginInfoProvider("TwilightLib-Velocity",
                LibraryVersion.VERSION,
                server.getVersion().getName() + " " + server.getVersion().getVersion());
        this.logger = logger;
        this.path = path;

        logger.info(pluginInfo.getStartupMessage());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            RedisConnector.initJedis(this);
        } catch (ConfigurateException e) {
            log().error("Unable to load Redis", e);
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        try {
            RedisConnector.shutdown();
        } catch (IOException e) {
            log().error("Unable to unload Redis", e);
        }
    }

    @Override
    public ConfigLoader getConfigLoader() {
        return null;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Logger log() {
        return logger;
    }

    @Override
    public boolean reload() {
        return false;
    }
}
