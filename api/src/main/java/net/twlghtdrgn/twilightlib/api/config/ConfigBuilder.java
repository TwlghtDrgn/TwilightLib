package net.twlghtdrgn.twilightlib.api.config;

import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple configuration builder used in {@link ConfigLoader}
 */
@SuppressWarnings("unused")
public class ConfigBuilder {
    private String configName;
    private final List<Row> rows = new ArrayList<>();

    /**
     * Creates a default config for later usage
     */
    public Conf createConfig() throws SerializationException {
        ConfigurationLoader<CommentedConfigurationNode> loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        ConfigurationNode node = loader.createNode();
        for (Row r:rows) {
            node.node(r.getName()).set(r.getVal());
        }
        return new Conf(configName, node);
    }

    /**
     * Creates a new {@link ConfigBuilder} instance
     */
    @Contract(" -> new")
    public static @NotNull Builder newBuilder() {
        return new ConfigBuilder().new Builder();
    }

    /**
     * Allows to build a configuration file
     */
    public class Builder {
        private Builder() {}

        /**
         * Set configuration file name
         * @param name config name
         * @return this builder
         */
        public Builder name(String name) {
            configName = name;
            return this;
        }

        /**
         * Adds a new row into a config file
         * @param val value
         * @param name Path name
         * @return this builder
         */
        public Builder addRow(Object val, Object... name) {
            Row row = new Row();
            row.setVal(val);
            row.setName(name);
            rows.add(row);
            return this;
        }

        /**
         * @return built configuration structure
         */
        public ConfigBuilder build() {
            return ConfigBuilder.this;
        }
    }

    public static class Row {
        private Object[] name;
        @Setter
        private Object val;
        public Object[] getName() {
            return name;
        }
        public void setName(Object... name) {
            this.name = name;
        }
        public Object getVal() {
            return val;
        }
    }

    public static class Conf {
        public Conf(String n, ConfigurationNode no) {
            name = n;
            node = no;
        }

        private final String name;
        private final ConfigurationNode node;
        public String getName() {
            return name;
        }
        public ConfigurationNode getNode() {
            return node;
        }
    }
}
