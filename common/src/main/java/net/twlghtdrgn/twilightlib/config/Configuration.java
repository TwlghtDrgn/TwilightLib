package net.twlghtdrgn.twilightlib.config;

import net.twlghtdrgn.twilightlib.ILibrary;
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
            final CommentedConfigurationNode originalNode = node.copy();
            final CommentedConfigurationNode fileNode = yamlLoader.load();
            node.set(clazz, config);

            final CommentedConfigurationNode diff = yamlLoader.createNode();

            if (!fileNode.equals(originalNode)) {
                for (var map:fileNode.childrenMap().entrySet()) {
                    if (fileNode.node(map.getKey()).equals(originalNode.node(map.getKey()))) continue;
                    diff.node(map.getKey()).set(map.getValue());
                }
            }

            if (!node.equals(originalNode)) {
                for (var map:node.childrenMap().entrySet()) {
                    if (node.node(map.getKey()).equals(originalNode.node(map.getKey()))) continue;
                    diff.node(map.getKey()).set(map.getValue());
                }
            }

            if (!diff.empty()) {
                for (var map:diff.childrenMap().entrySet())
                    node.node(map.getKey()).set(map.getValue());
            }
        } else node = yamlLoader.load();

        config = node.get(clazz);
        node.set(clazz, config);
        yamlLoader.save(node);
    }

    public T get() {
        return config;
    }
}
