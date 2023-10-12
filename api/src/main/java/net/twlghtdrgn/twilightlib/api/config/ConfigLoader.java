package net.twlghtdrgn.twilightlib.api.config;

import net.twlghtdrgn.twilightlib.api.ILibrary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that contains custom config loaders
 */
@SuppressWarnings("unused")
public class ConfigLoader {
    private final ILibrary library;
    private final Set<AbstractConfigurationFile> configurationFiles = new HashSet<>();
    private final Set<AbstractConfig> legacyConfiguration = new HashSet<>();
    public ConfigLoader(ILibrary lib) {
        this.library = lib;
    }

    /**
     * An ultra-simple version of modern Sponge Configurate config loader
     * @param builder {@link ConfigBuilder} instance that contains default config and config name
     * @return {@link ConfigurationNode}
     * @throws ConfigurateException if unable to load or save a file
     */
    public @NotNull ConfigurationNode load(final @NotNull ConfigBuilder builder) throws ConfigurateException {
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
     * @throws ConfigurateException if unable to load or save a file
     */
    @Nullable
    public Object load(final @NotNull AbstractConfig config) throws ConfigurateException {
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
            if (!configurationFiles.isEmpty())
                for (AbstractConfigurationFile c:configurationFiles)
                    c.reload();

            if (!legacyConfiguration.isEmpty())
                for (AbstractConfig c:legacyConfiguration)
                    c.reload();

            return true;
        } catch (ConfigurateException e) {
            library.log().error("Configuration files cannot be loaded", e);
            return false;
        }
    }

    public void addConfig(AbstractConfigurationFile configurationFile) {
        configurationFiles.add(configurationFile);
    }

    public void addConfig(AbstractConfig legacyConfigFile) {
        legacyConfiguration.add(legacyConfigFile);
    }
}
