package net.twlghtdrgn.twilightlib.config;

import lombok.Getter;
import org.spongepowered.configurate.ConfigurateException;

/**
 * An abstract configuration class used in {@link ConfigLoader}
 * @deprecated in favor of {@link Configuration}
 */
@Deprecated(since = "0.23.3")
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
