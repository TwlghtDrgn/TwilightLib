package net.twlghtdrgn.twilightlib.config;

import lombok.Getter;
import lombok.Setter;
import net.twlghtdrgn.twilightlib.ILibrary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that contains custom Bukkit and Sponge config loaders
 */
@SuppressWarnings("unused")
public class ConfigLoader {
    private final ILibrary library;
    @Getter
    @Setter
    private AbstractConfig[] configs;
    public ConfigLoader(ILibrary library) {
        this.library = library;
    }

    /**
     * An ultra-simple version of modern Sponge Configurate config loader
     * @param builder {@link ConfigBuilder} instance that contains default config and config name
     * @return {@link ConfigurationNode}
     * @throws IOException if unable to load or save a file
     */
    public @NotNull ConfigurationNode load(final @NotNull ConfigBuilder builder) throws IOException {
        ConfigBuilder.Conf virtualNode = builder.createConfig();
        Path path = Paths.get(library.getPath().toString(), virtualNode.getName());

        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder()
                .path(path)
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
        Path path = Paths.get(library.getPath().toString(), config.getConfigName());
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

    public boolean reload() {
        try {
            for (AbstractConfig config:configs)
                config.reload();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
