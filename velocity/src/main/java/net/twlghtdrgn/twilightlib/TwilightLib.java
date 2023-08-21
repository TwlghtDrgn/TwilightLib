package net.twlghtdrgn.twilightlib;

import com.velocitypowered.api.plugin.Plugin;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import net.twlghtdrgn.twilightlib.api.config.ConfigLoader;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "twilightlib",
        name = "TwilightLib"
)
public class TwilightLib implements ILibrary {
    @Override
    public ConfigLoader getConfigLoader() {
        return null;
    }

    @Override
    public Path getPath() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }
}
