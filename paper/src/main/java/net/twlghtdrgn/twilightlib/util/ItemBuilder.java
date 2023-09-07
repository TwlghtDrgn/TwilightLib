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
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private final Component name;
    private final List<Component> description;
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material, String name) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = new ArrayList<>();
    }

    public ItemBuilder(@NotNull ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = new ArrayList<>();
    }

    public ItemBuilder(Material material, String name, @NotNull String description) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = Arrays.stream(description.split("<newline>"))
                .map(Format::parse).toList();
    }

    public ItemBuilder(@NotNull ItemStack itemStack, String name, @NotNull String description) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.name = Format.parse(name);
        this.description = Arrays.stream(description.split("<newline>"))
                .map(Format::parse).toList();
    }

    public ItemBuilder setAmount(int amount) {
        if (amount <= 0) itemStack.setAmount(1);
        else itemStack.setAmount(Math.min(amount, 64));
        return this;
    }

    public ItemBuilder setEnchantments(PreparedEnchantment @NotNull ... preparedEnchantments) {
        for (PreparedEnchantment e:preparedEnchantments)
            itemMeta.addEnchant(e.getEnchantment(), e.getLevel(), true);
        return this;
    }

    public ItemBuilder setModel(int modelID) {
        itemMeta.setCustomModelData(modelID);
        return this;
    }

    public ItemStack build() {
        itemMeta.displayName(name);
        itemMeta.lore(description);
        itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }

    @Getter
    @AllArgsConstructor
    public static class PreparedEnchantment {
        private Enchantment enchantment;
        private int level;
    }
}
