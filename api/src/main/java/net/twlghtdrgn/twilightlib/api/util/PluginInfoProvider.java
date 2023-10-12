package net.twlghtdrgn.twilightlib.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Contains plugin info such as plugin name, version and its website
 */
@Getter
@AllArgsConstructor
public class PluginInfoProvider {
    private final String pluginName;
    private final String pluginVersion;
    private final String serverVersion;
    private final String website;

    /**
     * Shown on plugin startup. Contains plugin name, version, server version and a website
     */
    @NotNull
    public String getStartupMessage() {
        return """
            
            || %pluginName%
            ||
            || Version: %pluginVersion%
            || Server version: %serverVersion%
            || Plugin website: %website%"""
                .replace("%pluginName%", pluginName)
                .replace("%pluginVersion%", pluginVersion)
                .replace("%serverVersion%", serverVersion)
                .replace("%website%", website);
    }
}
