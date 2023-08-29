package net.twlghtdrgn.twilightlib.api.config;

import lombok.Getter;
import net.twlghtdrgn.twilightlib.api.ILibrary;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An abstract configuration class used in {@link ConfigLoader}
 */
public abstract class AbstractConfigurationFile {
    private final ILibrary library;
    private String configurationFileName;
    private Class<?> configurationFileClass;
    private YamlConfigurationLoader loader;
    private CommentedConfigurationNode node;
    @Getter
    private static Object config;

    protected AbstractConfigurationFile(ILibrary library) {
        this.library = library;
    }

    protected AbstractConfigurationFile(ILibrary library, String configurationFileName, Class<?> configurationFileClass) throws ConfigurateException {
        this.library = library;
        this.configurationFileName = configurationFileName;
        this.configurationFileClass = configurationFileClass;
        load();
    }

    public void load() throws ConfigurateException, NullPointerException {
        if (configurationFileName == null)
            throw new NullPointerException("Name of configuration file is not set");
        if (configurationFileClass == null)
            throw new NullPointerException("Class of configuration file is not set");

        Path path = Paths.get(library.getPath().toString(), configurationFileName);
        loader = YamlConfigurationLoader.builder()
                .path(path)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        reload();
    }

    public void reload() throws ConfigurateException {
        if (config != null) {
            node.set(configurationFileClass, config);
            final CommentedConfigurationNode tempNode = node.copy();
            node = loader.load();

            node.mergeFrom(tempNode);
        } else node = loader.load();
        setConfig(node.get(configurationFileClass));
        save();
    }

    public void save() throws ConfigurateException {
        node.set(configurationFileClass, config);
        loader.save(node);
    }

    private static void setConfig(Object c) {
        config = c;
    }
}
