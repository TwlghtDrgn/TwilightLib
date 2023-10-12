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
 * A set of formatters that are used in my plugins
 * @author TwlghtDrgn
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class Format {
    private Format() {}
    private static final String ERROR_AS_STRING = "parsing error";
    private static final Component ERROR_AS_COMPONENT = parse("<red>" + ERROR_AS_STRING);

    /**
     * Converts text to a component
     * @param in a string to convert
     * @return a component
     */
    @NotNull
    public static Component parse(@NotNull String in) {
        if (in.contains("§") || in.contains("&")) in = fromLegacy(in);
        return MiniMessage.miniMessage().deserializeOr(in, ERROR_AS_COMPONENT)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts component to a text
     * @param component a component to convert
     * @return a regular string without any formatting
     */
    public static String parse(Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component,ERROR_AS_STRING);
    }

    /**
     * Converts string to a Minecraft JSON string
     * @param string a string to convert
     * @return a JSON-component
     */
    @NotNull
    public static Component gson(@NotNull String string) {
        if (string.contains("§") || string.contains("&")) string = fromLegacy(string);
        return GsonComponentSerializer.gson().deserializeOr(string, ERROR_AS_COMPONENT)
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

    private static final Map<String, String> replacements = new HashMap<>();

    static {
        for (int i = 0; i < legacyColors.length; i++) {
            for (String s:legacySymbols)
                replacements.put(s + legacyColors[i], miniColors[i]);
        }
    }

    /**
     * Allows you to convert legacy color codes to a MiniMessage colors
     * @param in to convert
     * @return converted string
     */
    public static String fromLegacy(String in) {
        for (var entry:replacements.entrySet()) {
            in = in.replace(entry.getKey(), entry.getValue());
        }
        return in;
    }
}
