package net.twlghtdrgn.twilightlib.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class PluginInfoProvider {
    private final String pluginName;
    private final String pluginVersion;
    private final String serverVersion;
    private final String website;

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
