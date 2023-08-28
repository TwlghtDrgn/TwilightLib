package net.twlghtdrgn.twilightlib.exception;

/**
 * Replaces generic Exception
 */
@SuppressWarnings("unused")
public class PluginLoadException extends Exception {
    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }
}
