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
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.LibraryInfo;
import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.redis.RedisConnector;
import net.twlghtdrgn.twilightlib.api.util.PluginInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "twilightlib",
        name = LibraryInfo.NAME,
        version = LibraryInfo.VERSION,
        authors = {"TwlghtDrgn"},
        url = LibraryInfo.URL,
        dependencies = {@Dependency(id = "miniplaceholders", optional = true)}
)
public class TwilightLib implements ILibrary {
    @Getter
    private final PluginInfoProvider pluginInfo;
    private final Logger logger;
    private final Path path;

    @Inject
    public TwilightLib(@NotNull Logger logger, @NotNull ProxyServer server, @DataDirectory Path path) {
        pluginInfo = new PluginInfoProvider(LibraryInfo.NAME,
                LibraryInfo.VERSION,
                server.getVersion().getName() + " " + server.getVersion().getVersion(),
                LibraryInfo.URL);
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
            RedisConnector.close();
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
