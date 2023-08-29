package net.twlghtdrgn.twilightlib.api.config;

import lombok.Getter;
import org.spongepowered.configurate.ConfigurateException;

/**
 * An abstract configuration class used in {@link ConfigLoader}
 */
public abstract class AbstractConfig {
    @Getter
    private final String configName;
    @Getter
    private final Class<?> configClass;

    protected AbstractConfig(String configName, Class<?> configClass) {
        this.configName = configName;
        this.configClass = configClass;
    }

    public abstract void reload() throws ConfigurateException;
}
