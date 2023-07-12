package net.twlghtdrgn.twilightlib.config;

import net.twlghtdrgn.twilightlib.TwilightPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that contains custom Bukkit and Sponge config loaders
 */
@SuppressWarnings("unused")
public class ConfigLoader {
    private final TwilightPlugin plugin;
    public ConfigLoader(TwilightPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Legacy Bukkit API config loader
     * @param config Config file name
     * @return {@link FileConfiguration}
     * @throws IOException if unable to load a resource
     */
    public FileConfiguration load(final String config) throws IOException {
        File dir = plugin.getDataFolder();
        File file = new File(dir, config);

        if (!file.exists()) plugin.saveResource(config,false);

        FileConfiguration cfg;

        cfg = YamlConfiguration.loadConfiguration(file);

        final InputStream is = plugin.getResource(config);
        if (is == null) throw new IOException("Unable to load config " + config + " from JAR");

        cfg.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8)));
        cfg.save(file);

        return cfg;
    }

    /**
     * An ultra-simple version of modern Sponge Configurate config loader
     * @param builder {@link ConfigBuilder} instance that contains default config and config name
     * @return {@link ConfigurationNode}
     * @throws IOException if unable to load or save a file
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

    /**
     * A Sponge Configurate config loader, simplified
     * @param config {@link AbstractConfig}
     * @return {@link Object} an object that should be cast as your config class
     * @throws IOException if unable to load or save a file
     */
    @Nullable
    public Object load(final @NotNull AbstractConfig config) throws IOException {
        Path path = Paths.get(plugin.getDataFolder().toString(), config.getConfigName());
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(path)
                .nodeStyle(NodeStyle.BLOCK)
                .build();

        CommentedConfigurationNode node = loader.load();
        Object cfg = node.get(config.getConfigClass());

        node.set(config.getConfigClass(), cfg);
        loader.save(node);
        return cfg;
    }
}
