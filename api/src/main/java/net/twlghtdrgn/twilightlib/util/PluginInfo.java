package net.twlghtdrgn.twilightlib.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class PluginInfo {
    @Nullable
    private static PluginInfo info;

    public PluginInfo(String pluginName, String pluginVersion, String serverVersion, String website) {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.serverVersion = serverVersion;
        this.website = website;

        info = this;
    }

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
            || Plugin website: %website%
            """
                .replace("%pluginName%", pluginName)
                .replace("%pluginVersion%", pluginVersion)
                .replace("%serverVersion%", serverVersion)
                .replace("%website%", website);
    }
}
