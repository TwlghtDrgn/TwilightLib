package net.twlghtdrgn.twilightlib.config;

import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.exception.ConfigLoadException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Class that contains custom Bukkit and Sponge config loaders
 */
@SuppressWarnings("unused")
public class Config {
    private final TwilightPlugin plugin;
    public Config(TwilightPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Legacy Bukkit API config loader
     * @param config Config file name
     * @return {@link FileConfiguration}
     * @throws ConfigLoadException if unable to load a resource
     */
    public FileConfiguration legacy(final String config) throws ConfigLoadException, IOException {
        File dir = plugin.getDataFolder();
        File file = new File(dir, config);

        if (!file.exists()) plugin.saveResource(config,false);

        FileConfiguration cfg;

        cfg = YamlConfiguration.loadConfiguration(file);

        final InputStream is = plugin.getResource(config);
        if (is == null) throw new ConfigLoadException(config);

        cfg.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8)));
        cfg.save(file);

        return cfg;
    }

    /**
     * Modern Sponge Configurate config loader
     * @param builder {@link ConfigBuilder} instance that contains default config and config name
     * @return {@link ConfigurationNode}
     * @throws IOException if unable to save a file
     */
    public @NotNull ConfigurationNode load(final @NotNull ConfigBuilder builder) throws IOException {
        ConfigBuilder.Conf virtualNode = builder.createConfig();
        File dir = plugin.getDataFolder();
        File file = new File(dir,virtualNode.getName());

        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        ConfigurationNode node = loader.load();

        node.mergeFrom(virtualNode.getNode());
        loader.save(node);

        return node;
    }
}
