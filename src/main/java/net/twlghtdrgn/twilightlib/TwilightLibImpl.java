package net.twlghtdrgn.twilightlib;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TwilightLibImpl {
    public TwilightLibImpl(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Getter
    private static JavaPlugin plugin;
}
