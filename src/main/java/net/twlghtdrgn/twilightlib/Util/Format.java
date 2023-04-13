package net.twlghtdrgn.twilightlib.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * A collection of {@link MiniMessage} formatters used
 * @author TwlghtDrgn
 */
public class Format {
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final PlainTextComponentSerializer ptcs = PlainTextComponentSerializer.plainText();
    private static final GsonComponentSerializer gcs = GsonComponentSerializer.gson();

    /**
     * Uses {@link MiniMessage} to convert {@link String} into a {@link Component}
     * @param s {@link String} to Componentize
     * @return {@link Component} deserialized as {@link MiniMessage} {@link Component}
     */
    public static Component parse(String s) {
        return mm.deserializeOrNull(s)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Uses {@link PlainTextComponentSerializer} to convert {@link Component} into a {@link String}
     * @param c {@link Component} to Stringize
     * @return {@link String} serialized to usual text
     */
    public static String parse(Component c) {
        return ptcs.serializeOrNull(c);
    }

    /**
     * Uses {@link GsonComponentSerializer} to convert standard string into a Minecraft's {@link com.google.gson.Gson} representation
     * @param s {@link String} to JSONize
     * @return {@link Component} deserialized as its {@link com.google.gson.Gson} representation
     */
    public static Component gson(String s) {
        return gcs.deserializeOrNull(s)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}
