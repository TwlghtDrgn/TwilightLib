package net.twlghtdrgn.twilightlib.util;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private static boolean isMiniPlaceholdersPresent;
    static {
        try {
            Class.forName("io.github.miniplaceholders.api.MiniPlaceholders");
            isMiniPlaceholdersPresent = true;
        } catch (ClassNotFoundException ignored) {
            isMiniPlaceholdersPresent = false;
        }
    }

    /**
     * Converts text to a component
     * @param in a string to convert. If installed, {@link MiniPlaceholders} placeholders can be used
     * @return a component
     */
    public static @NotNull Component parse(@NotNull String in) {
        if (in.contains("§") || in.contains("&")) in = fromLegacy(in);

        if (isMiniPlaceholdersPresent) {
            return MiniMessage.miniMessage().deserialize(in, MiniPlaceholders.getGlobalPlaceholders())
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        } else {
            return MiniMessage.miniMessage().deserialize(in)
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        }
    }

    /**
     * Converts text to a component
     * @param in a string to convert
     * @param audience an audience for {@link MiniPlaceholders}. If you don't have it installed, then use parse(String)
     * @return a component
     */
    public static @NotNull Component parse(@NotNull String in, @Nullable Audience audience) {
        if (in.contains("§") || in.contains("&")) in = fromLegacy(in);

        if (isMiniPlaceholdersPresent && audience != null) {
            return MiniMessage.miniMessage().deserialize(in,
                            MiniPlaceholders.getGlobalPlaceholders(),
                            MiniPlaceholders.getAudienceGlobalPlaceholders(audience),
                            MiniPlaceholders.getAudiencePlaceholders(audience))
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        } else {
            return parse(in);
        }
    }

    /**
     * Converts component to a text
     * @param component a component to convert
     * @return a regular string without any formatting
     */
    public static @NotNull String parse(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    /**
     * Converts string to a Minecraft JSON string
     * @param string a string to convert
     * @return a JSON-component
     */
    public static @NotNull Component gson(@NotNull String string) {
        if (string.contains("§") || string.contains("&")) string = fromLegacy(string);
        return GsonComponentSerializer.gson().deserialize(string)
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
