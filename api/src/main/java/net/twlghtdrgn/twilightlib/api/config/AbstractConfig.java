package net.twlghtdrgn.twilightlib.api.config;

import lombok.Getter;
import org.spongepowered.configurate.ConfigurateException;

/**
 * An abstract configuration class used in {@link ConfigLoader}
 */
@Getter
public abstract class AbstractConfig {
    private final String configName;
    private final Class<?> configClass;

    protected AbstractConfig(String configName, Class<?> configClass) {
        this.configName = configName;
        this.configClass = configClass;
    }

    public abstract void reload() throws ConfigurateException;
}
