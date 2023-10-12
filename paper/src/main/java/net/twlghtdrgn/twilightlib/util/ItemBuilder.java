package net.twlghtdrgn.twilightlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.twlghtdrgn.twilightlib.api.util.Format;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder for the items
 * @author TwlghtDrgn
 * @since 0.0.19e
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private final Component name;
    private final List<Component> description;
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    /**
     * Builds an item using {@link ItemStack}
     * @param material material of an item
     * @param name name of the item. Uses MiniMessage formatting
     */
    public ItemBuilder(Material material, String name) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = new ArrayList<>();
    }

    /**
     * Builds an item using {@link ItemStack}
     * @param itemStack ItemStack of an item
     * @param name name of the item. Uses MiniMessage formatting
     */
    public ItemBuilder(@NotNull ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = new ArrayList<>();
    }

    /**
     * Builds an item using {@link Material}
     * @param material material of an item
     * @param name name of the item. Uses MiniMessage formatting
     * @param description A lore of the item
     */
    public ItemBuilder(Material material, String name, @NotNull String description) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = Arrays.stream(description.split("<newline>"))
                .map(Format::parse).toList();
    }

    /**
     * Builds an item using {@link ItemStack}
     * @param itemStack ItemStack of an item
     * @param name name of the item. Uses MiniMessage formatting
     * @param description A lore of the item
     */
    public ItemBuilder(@NotNull ItemStack itemStack, String name, @NotNull String description) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = Arrays.stream(description.split("<newline>"))
                .map(Format::parse).toList();
    }

    /**
     * Sets an amount of items in the stack
     * @param amount an amount of items that will this item contain
     */
    public ItemBuilder setAmount(int amount) {
        if (amount <= 0) itemStack.setAmount(1);
        else itemStack.setAmount(Math.min(amount, 64));
        return this;
    }

    /**
     * Adds a list of enchantments to the item.
     * @param preparedEnchantments an array of {@link PreparedEnchantment}
     */
    public ItemBuilder setEnchantments(PreparedEnchantment @NotNull ... preparedEnchantments) {
        for (PreparedEnchantment e:preparedEnchantments)
            itemMeta.addEnchant(e.getEnchantment(), e.getLevel(), true);
        return this;
    }

    /**
     * Adds a model to the item
     */
    public ItemBuilder setModel(int modelID) {
        itemMeta.setCustomModelData(modelID);
        return this;
    }

    /**
     * Builds an item
     * @return a new {@link ItemStack}
     */
    public ItemStack build() {
        itemMeta.displayName(name);
        itemMeta.lore(description);
        itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }

    /**
     * The easiest way to add an enchantment into the {@link ItemBuilder}
     */
    @Getter
    @AllArgsConstructor
    public static class PreparedEnchantment {
        private Enchantment enchantment;
        private int level;
    }
}
