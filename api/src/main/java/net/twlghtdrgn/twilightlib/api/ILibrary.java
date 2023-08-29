package net.twlghtdrgn.twilightlib.api;

import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfo;

import java.nio.file.Path;

@SuppressWarnings("unused")
public interface ILibrary {
    ConfigLoader getConfigLoader();
    Path getPath();
    PluginInfo getPluginInfo();
}
