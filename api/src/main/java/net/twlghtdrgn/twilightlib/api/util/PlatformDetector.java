package net.twlghtdrgn.twilightlib.api.util;

public class PlatformDetector {
    private static Platform platform = null;

    public static Platform getPlatform() {
        if (platform == null) platform = currentPlatform();
        return platform;
    }

    private static Platform currentPlatform() {
        try {
            Class.forName("com.velocitypowered.api.proxy.ProxyServer");
            return Platform.VELOCITY;
        } catch (ClassNotFoundException ignored) {
            // Not a Velocity
        }

        try {
            Class.forName("io.papermc.paper.plugin.loader.PluginLoader");
            return Platform.PAPER;
        } catch (ClassNotFoundException ignored) {
            // Not a Paper
        }
        return Platform.UNKNOWN;
    }

    public enum Platform {
        PAPER,
        VELOCITY,
        UNKNOWN
    }
}
