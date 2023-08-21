package net.twlghtdrgn.twilightlib.api;

import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;

import java.nio.file.Path;
import java.util.logging.Logger;

public interface ILibrary {
    ConfigLoader getConfigLoader();
    Path getPath();
    Logger getLogger();
}
