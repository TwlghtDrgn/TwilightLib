package net.twlghtdrgn.twilightlib.api;

import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.api.util.PluginInfoProvider;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * A main interface of the Library
 * Implement this if you are using Velocity
 * Extend TwilightPlugin from a package "TwilightLib-Paper" if your plugin is intended to work on Paper
 * @author TwlghtDrgn
 * @since 0.0.19-SNAPSHOT
 */
@SuppressWarnings("unused")
public interface ILibrary {
    ConfigLoader getConfigLoader();
    Path getPath();
    PluginInfoProvider getPluginInfo();
    Logger log();
    boolean reload();
}
