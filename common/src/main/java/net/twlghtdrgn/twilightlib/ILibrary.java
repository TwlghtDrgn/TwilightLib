package net.twlghtdrgn.twilightlib;

import net.twlghtdrgn.twilightlib.config.ConfigLoader;
import net.twlghtdrgn.twilightlib.config.Configuration;
import net.twlghtdrgn.twilightlib.util.PluginInfoProvider;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * A main interface of the Library
 * @author TwlghtDrgn
 * @since 0.0.19-SNAPSHOT
 */
@SuppressWarnings("unused")
public interface ILibrary {
    /**
     * A configuration loader
     * @deprecated in favor of {@link Configuration}
     */
    @Deprecated(since = "0.23.3")
    ConfigLoader getConfigLoader();

    /**
     * A plugin data folder
     */
    Path getPath();

    /**
     * @see PluginInfoProvider
     */
    PluginInfoProvider getPluginInfo();

    /**
     * An SL4J logger
     */
    Logger log();

    /**
     * Reloads all main config files
     */
    boolean reload();
}
