package net.twlghtdrgn.twilightlib.api.config;

import lombok.Getter;
import lombok.Setter;
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
    @Getter
    @Setter
    private String configurationFileName;
    @Getter
    @Setter
    private Class<?> configurationFileClass;
    private YamlConfigurationLoader loader;
    private CommentedConfigurationNode node;
    @Getter
    @Setter
    private static Object config;

    protected AbstractConfigurationFile(ILibrary library) {
        try {
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
        } catch (NullPointerException | ConfigurateException e) {
            e.printStackTrace();
        }
    }

    public void reload() throws ConfigurateException {
        if (config != null) save();
        node = loader.load();
        setConfig(node.get(configurationFileClass));
        save();
    }

    public void save() throws ConfigurateException {
        node.set(configurationFileClass, config);
        loader.save(node);
    }
}
