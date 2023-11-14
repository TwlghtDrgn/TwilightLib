package net.twlghtdrgn.twilightlib.util;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.chat.contents.*;
import net.twlghtdrgn.twilightlib.api.util.Format;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ComponentUtil {
    private ComponentUtil() {}

    public static <T> Object convert(T object) {
        if (object == null) return null;
        if (object instanceof IChatBaseComponent baseComponent) return nmsToAdventureJson(baseComponent);
        else if (object instanceof Component adventureComponent) return adventureToAdventure(adventureComponent);
        else if (object instanceof String string) {
            String extracted = new Gson().fromJson(string, Actionbar.class).text;
            return GsonComponentSerializer.gson().serialize(Format.parse(extracted));
        }
        else return object;
    }

    public static IChatBaseComponent nmsToAdventureJson(IChatBaseComponent baseComponent) {
        return IChatBaseComponent.ChatSerializer.a(GsonComponentSerializer.gson().serialize(nmsToAdventure(baseComponent)));
    }

    public static Component nmsToAdventure(@NotNull IChatBaseComponent baseComponent) {
        ComponentContents contents = baseComponent.b();
        Component adventureComponent;

        if (contents instanceof KeybindContents keybindContents) {
            adventureComponent = Component.keybind(keybindContents.a());
        } else if (contents instanceof LiteralContents literalContents) {
            adventureComponent = Format.parse(literalContents.a());
        }
        // TODO: NbtContents:
        //        } else if (cc instanceof NbtContents nbt) {
        //     if (nbt.c().isPresent()) {
        //        adventureComponent = Component.blockNBT(nbt.a(), nbt.b(), Component.text(((LiteralContents) nbt.c().get().b()).a()), nbt.d(), nbt.d().);
        //    }
        else if (contents instanceof ScoreContents scoreContents) {
            adventureComponent = Component.score(scoreContents.a(), scoreContents.c());
        } else if (contents instanceof SelectorContents selectorContents) {
            if (selectorContents.c().isPresent()) {
                adventureComponent = Component.selector(selectorContents.a(), nmsToAdventure(selectorContents.c().get()));
            } else adventureComponent = Component.selector(selectorContents.a());
        } else if (contents instanceof TranslatableContents translatableContents) {
            adventureComponent = Component.translatable(translatableContents.a());
        } else adventureComponent = Component.empty();

        if (!baseComponent.c().isEmpty()) {
            final List<Component> children = new ArrayList<>();
            baseComponent.c().forEach(c -> children.add(nmsToAdventure(c)));
            adventureComponent = adventureComponent.children(children);
        }

        return adventureComponent;
    }

    public static Component adventureToAdventure(Component adventureComponent) {
        if (adventureComponent instanceof TextComponent textComponent) {
            String raw = MiniMessage.miniMessage().serialize(clickEventRemover(textComponent))
                    .replace("\\<","<");
            return Format.parse(raw);
        }
        return adventureComponent;
    }

    private static Component clickEventRemover(@NotNull Component component) {
        final List<Component> children = new ArrayList<>(component.children());
        if (children.isEmpty()){
            component = component.clickEvent(null);
            return component;
        }
        for (int i = 0; i < children.size(); i++)
            children.set(i, clickEventRemover(children.get(i)));
        return component.children(children);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Actionbar {
        private String text;
    }
}
