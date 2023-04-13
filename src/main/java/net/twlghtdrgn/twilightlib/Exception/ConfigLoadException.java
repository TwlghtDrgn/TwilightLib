package net.twlghtdrgn.twilightlib.Exception;

/**
 * Thrown if config cannot be loaded by some reason
 */
public class ConfigLoadException extends Exception {
    public ConfigLoadException(String message, Throwable cause) {
        super("Unable to load config: " + message, cause);
    }

    public ConfigLoadException(Throwable cause) {
        super(cause);
    }

    public ConfigLoadException(String message) {
        super("Unable to load config: " + message);
    }
}
