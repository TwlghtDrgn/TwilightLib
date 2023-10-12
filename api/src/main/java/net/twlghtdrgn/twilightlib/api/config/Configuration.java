package net.twlghtdrgn.twilightlib.api.config;

import net.twlghtdrgn.twilightlib.api.ILibrary;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Paths;

/**
 * A configuration file wrapper.
 * Requires a class with {@link org.spongepowered.configurate.objectmapping.ConfigSerializable} annotation
 * @author TwlghtDrgn
 * @version 0.2.0
 */
@SuppressWarnings("unused")
public class Configuration<T> {
    private T config;
    private final Class<T> clazz;
    private final YamlConfigurationLoader yamlLoader;
    private CommentedConfigurationNode node;
    public Configuration(ILibrary library, Class<T> clazz) {
        this(library, clazz, clazz.getSimpleName().toLowerCase());
    }

    public Configuration(@NotNull ILibrary library, Class<T> clazz, String configName) {
        this.clazz = clazz;
        this.yamlLoader = YamlConfigurationLoader.builder()
                .path(Paths.get(library.getPath().toString(), configName + ".yml"))
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }

    public void reload() throws ConfigurateException {
        if (node != null && config != null) {
            node.set(clazz, config);
            final CommentedConfigurationNode tempNode = node.copy();
            node = yamlLoader.load();
            node.mergeFrom(tempNode);
        } else node = yamlLoader.load();

        config = node.get(clazz);
        node.set(clazz, config);
        yamlLoader.save(node);
    }

    public T get() {
        return config;
    }
}
