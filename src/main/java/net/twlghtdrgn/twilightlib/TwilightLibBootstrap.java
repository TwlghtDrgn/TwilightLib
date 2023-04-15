package net.twlghtdrgn.twilightlib;

import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TwilightLibBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull PluginProviderContext context) {
        // Currently does nothing
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new TwilightLib();
    }
}
