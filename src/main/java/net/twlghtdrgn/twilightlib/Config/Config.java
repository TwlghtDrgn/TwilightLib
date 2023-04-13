package net.twlghtdrgn.twilightlib.Config;

import com.google.common.base.Charsets;
import net.twlghtdrgn.twilightlib.Exception.ConfigLoadException;
import net.twlghtdrgn.twilightlib.TwilightLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {
    /**
     * Loads new config files for later usage
     * @param config Config file name
     * @return {@link FileConfiguration}
     * @throws ConfigLoadException if something went wrong
     */
    public static FileConfiguration load(final String config) throws ConfigLoadException, IOException {
        final JavaPlugin plugin = TwilightLib.getPlugin();
        File dir = plugin.getDataFolder();
        File file = new File(dir, config);

        if (!file.exists()) plugin.saveResource(config,false);

        FileConfiguration cfg;

        cfg = YamlConfiguration.loadConfiguration(file);

        final InputStream is = plugin.getResource(config);
        if (is == null) throw new ConfigLoadException(config);

        cfg.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(is, Charsets.UTF_8)));
        cfg.save(file);

        return cfg;
    }
}
