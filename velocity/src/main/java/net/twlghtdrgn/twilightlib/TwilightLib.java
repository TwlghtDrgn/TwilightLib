package net.twlghtdrgn.twilightlib;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.LibraryInfo;
import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "twilightlib",
        name = LibraryInfo.NAME,
        version = LibraryInfo.VERSION,
        authors = {"TwlghtDrgn"},
        url = LibraryInfo.URL
)
public class TwilightLib implements ILibrary {
    @Getter
    private final PluginInfoProvider pluginInfo;

    @Inject
    public TwilightLib(@NotNull Logger logger, @NotNull ProxyServer server) {
        pluginInfo = new PluginInfoProvider(LibraryInfo.NAME,
                LibraryInfo.VERSION,
                server.getVersion().getName() + " " + server.getVersion().getVersion(),
                LibraryInfo.URL);

        logger.info(pluginInfo.getStartupMessage());
    }

    @Override
    public ConfigLoader getConfigLoader() {
        return null;
    }

    @Override
    public Path getPath() {
        return null;
    }

    @Override
    public Logger log() {
        return null;
    }

    @Override
    public boolean reload() {
        return false;
    }
}
