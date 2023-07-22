package net.twlghtdrgn.twilightlib;

import net.twlghtdrgn.twilightlib.config.ConfigLoader;

import java.nio.file.Path;
import java.util.logging.Logger;

public interface ILibrary {
    ConfigLoader getConfigLoader();
    Path getPath();
    Logger getLogger();
}
