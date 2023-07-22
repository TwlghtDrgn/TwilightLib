package net.twlghtdrgn.twilightlib.config;

import lombok.Getter;

import java.io.IOException;

public abstract class AbstractConfig {
    @Getter
    private final String configName;
    @Getter
    private final Class<?> configClass;

    protected AbstractConfig(String configName, Class<?> configClass) {
        this.configName = configName;
        this.configClass = configClass;
    }

    public abstract void reload() throws IOException;
}
