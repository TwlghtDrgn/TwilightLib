package net.twlghtdrgn.twilightlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * A builder for the items
 * @author TwlghtDrgn
 * @since 0.0.19e
 */
@SuppressWarnings("unused")
public class ItemBuilder {
    private final ItemStack itemStack;
    private Component itemName;
    private final List<Component> itemLore = new ArrayList<>();
    private boolean replaceLore = false;

    /**
     * Build an item using {@link Material}
     * @param material material of an item
     * @see ItemBuilder#ItemBuilder(ItemStack)
     */
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    /**
     * Build an item using {@link ItemStack}
     * @param itemStack an item
     * @see ItemBuilder#ItemBuilder(Material)
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Set an amount
     * @param amount of items this {@link ItemStack} will hold
     * @see ItemBuilder#amount(int, boolean)
     */
    public ItemBuilder amount(int amount) {
        amount(amount, false);
        return this;
    }

    /**
     * Set an amount
     * @param amount of items this {@link ItemStack} will hold
     * @param oversized set to true, if you want to hold more than 64 items
     * @see ItemBuilder#amount(int)
     */
    public ItemBuilder amount(int amount, boolean oversized) {
        if (amount <= 0) {
            this.itemStack.setAmount(1);
            return this;
        }

        if (oversized) {
            this.itemStack.setAmount(amount);
        } else {
            this.itemStack.setAmount(Math.min(amount, 64));
        }

        return this;
    }


    /**
     * Add enchantments
     * @param preparedEnchantments a list of {@link PreparedEnchantment}
     */
    public ItemBuilder enchantments(PreparedEnchantment @NotNull ... preparedEnchantments) {
        for (PreparedEnchantment e:preparedEnchantments)
            meta().addEnchant(e.getEnchantment(), e.getLevel(), true);
        return this;
    }

    /**
     * Set a model
     */
    public ItemBuilder model(int customModelDataId) {
        meta().setCustomModelData(customModelDataId);
        return this;
    }

    /**
     * Set a new name for an item
     * @see ItemBuilder#name(Component)
     */
    public ItemBuilder name(String itemName) {
        this.itemName = Format.parse(itemName);
        return this;
    }

    /**
     * Set a new name for an item
     * @see ItemBuilder#name(String)
     */
    public ItemBuilder name(Component itemName) {
        this.itemName = itemName;
        return this;
    }

    /**
     * Add a lore strings
     * @see ItemBuilder#lore(boolean, Component...))
     * @see ItemBuilder#lore(String)
     * @see ItemBuilder#lore(Component)
     */
    public ItemBuilder lore(boolean isReplacement, String... lore) {
        this.replaceLore = isReplacement;
        this.itemLore.addAll(new ArrayList<>(Stream.of(lore)
                .map(Format::parse)
                .toList()));
        return this;
    }

    /**
     * Add a lore components
     * @see ItemBuilder#lore(boolean, String...))
     * @see ItemBuilder#lore(String)
     * @see ItemBuilder#lore(Component)
     */
    public ItemBuilder lore(boolean isReplacement, Component... lore) {
        this.replaceLore = isReplacement;
        this.itemLore.addAll(new ArrayList<>(List.of(lore)));
        return this;
    }

    /**
     * Add a lore string
     * @see ItemBuilder#lore(Component)
     * @see ItemBuilder#lore(boolean, String...)
     * @see ItemBuilder#lore(boolean, Component...)
     */
    public ItemBuilder lore(@NotNull String row) {
        this.itemLore.add(Format.parse(row));
        return this;
    }

    /**
     * Add a lore component
     * @see ItemBuilder#lore(String)
     * @see ItemBuilder#lore(boolean, String...)
     * @see ItemBuilder#lore(boolean, Component...)
     */
    public ItemBuilder lore(@NotNull Component row) {
        this.itemLore.add(row);
        return this;
    }

    /**
     * Whether to replace lore, or not
     * @see ItemBuilder#lore(boolean, String...)
     * @see ItemBuilder#lore(boolean, Component...)
     */
    public ItemBuilder replaceLore(boolean value) {
        this.replaceLore = value;
        return this;
    }

    /**
     * Builds an item
     * @return a new {@link ItemStack}
     */
    public ItemStack build() {
        if (this.itemName != null) meta().displayName(itemName);

        if (!this.itemLore.isEmpty()) {
            if (!this.replaceLore && this.meta().hasLore()) {
                this.itemLore.addAll(0, meta().lore());
                meta().lore(this.itemLore);
            } else {
                meta().lore(this.itemLore);
            }
        }

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

    private ItemMeta meta() {
        return itemStack.getItemMeta();
    }
}
