package net.twlghtdrgn.twilightlib.util;

import lombok.Getter;

/**
 * Checks a platform we're running on
 * @author TwlghtDrgn
 * @since 0.23.0
 */
@Getter
public class PlatformUtil {
    private static Platform platform = Platform.UNKNOWN;

    static {
        try {
            Class.forName("com.velocitypowered.api.proxy.ProxyServer");
            platform = Platform.VELOCITY;
        } catch (ClassNotFoundException ignored) {
            // Not a Velocity
        }

        try {
            Class.forName("io.papermc.paper.plugin.loader.PluginLoader");
            platform = Platform.PAPER;
        } catch (ClassNotFoundException ignored) {
            // Not a Paper
        }
    }

    public enum Platform {
        PAPER,
        VELOCITY,
        UNKNOWN
    }
}
