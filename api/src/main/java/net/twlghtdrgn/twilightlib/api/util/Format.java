package net.twlghtdrgn.twilightlib.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Formatters used in my plugins
 */
@SuppressWarnings("unused")
public class Format {
    private Format() {}

    /**
     * Converts text to a component
     * @param string a string to convert
     * @return a component
     */
    @NotNull
    public static Component parse(@NotNull String string) {
        if (string.contains("§") || string.contains("&")) string = fromLegacy(string);
        return MiniMessage.miniMessage().deserializeOr(string, Component.text("Error"))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts component to a text
     * @param component a component to convert
     * @return a regular string without any formatting
     */
    public static String parse(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component,"Error");
    }

    /**
     * Converts string to a Minecraft JSON string
     * @param string a string to convert
     * @return a JSON-component
     */
    @NotNull
    public static Component gson(@NotNull String string) {
        if (string.contains("§") || string.contains("&")) string = fromLegacy(string);
        return GsonComponentSerializer.gson().deserializeOr(string, Component.text("Error"))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts seconds into a human-readable time
     * @param t a time in seconds
     * @return a formatted time string (in 24hr format, HH:mm:ss)
     */
    public static String time(double t) {
        int hours = (int) t / 3600;
        int minutes = (int) (t % 3600) / 60;
        int seconds = (int) t % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static final String[] legacySymbols = {
            "§",
            "&"
    };

    private static final String[] legacyColors = {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",

            "k",
            "l",
            "m",
            "n",
            "o",
            "r"
    };

    private static final String[] miniColors = {
            "<black>",
            "<dark_blue>",
            "<dark_green>",
            "<dark_aqua>",
            "<dark_red>",
            "<dark_purple>",
            "<gold>",
            "<gray>",
            "<dark_gray>",
            "<blue>",
            "<green>",
            "<aqua>",
            "<red>",
            "<light_purple>",
            "<yellow>",
            "<white>",

            "<obfuscated>",
            "<bold>",
            "<strikethrough>",
            "<underlined>",
            "<italic>",
            "<reset>",
    };

    /**
     * Allows you to convert legacy color codes to a MiniMessage colors
     * @param string to convert
     * @return converted string
     */
    public static String fromLegacy(String string) {
        final Map<String, String> replacements = new HashMap<>();
        for (int i = 0; i < legacyColors.length; i++) {
            for (String s:legacySymbols)
                replacements.put(s + legacyColors[i], miniColors[i]);
        }

        for (var entry : replacements.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }

        return string;
    }
}
