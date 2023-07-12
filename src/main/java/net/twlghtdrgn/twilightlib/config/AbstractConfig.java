package net.twlghtdrgn.twilightlib.config;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

public abstract class AbstractConfig {
    @Getter
    private final String configName;
    @Getter
    private final Object configClass;

    @Getter
    @Nullable
    private static Cfg config;

    protected AbstractConfig(String configName, Object configClass) {
        this.configName = configName;
        this.configClass = configClass;
    }

    public abstract void reload();

    @ConfigSerializable
    public abstract class Cfg {

    }
}
