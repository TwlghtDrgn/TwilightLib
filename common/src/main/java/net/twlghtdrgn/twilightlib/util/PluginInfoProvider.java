package net.twlghtdrgn.twilightlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Contains plugin info such as plugin name and version
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public class PluginInfoProvider {
    private final String pluginName;
    private final String pluginVersion;
    private final String serverVersion;

    /**
     * Shown on plugin startup
     */
    @NotNull
    public String getStartupMessage() {
        return """
            
            || %pluginName%
            ||
            || Version: %pluginVersion%
            || Server version: %serverVersion%"""
                .replace("%pluginName%", pluginName)
                .replace("%pluginVersion%", pluginVersion)
                .replace("%serverVersion%", serverVersion);
    }
}
