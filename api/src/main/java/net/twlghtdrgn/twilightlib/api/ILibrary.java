package net.twlghtdrgn.twilightlib.api;

import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfoProvider;
import org.slf4j.Logger;

import java.nio.file.Path;

@SuppressWarnings("unused")
public interface ILibrary {
    ConfigLoader getConfigLoader();
    Path getPath();
    PluginInfoProvider getPluginInfo();
    Logger log();
    boolean reload();
}
