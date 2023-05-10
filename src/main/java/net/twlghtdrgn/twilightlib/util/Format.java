package net.twlghtdrgn.twilightlib.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

/**
 * Formatters used in my plugins
 * @author TwlghtDrgn
 * @version 0.0.1
 */
public class Format {
    private Format() {}
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer lcs = LegacyComponentSerializer.legacyAmpersand();
    private static final PlainTextComponentSerializer ptcs = PlainTextComponentSerializer.plainText();
    private static final GsonComponentSerializer gcs = GsonComponentSerializer.gson();

    /**
     * Converts text to a component
     * @param s a string to convert
     * @return a component
     */
    public static Component parse(String s) {
        if (s == null) return null;
        if (s.matches(".*&[0-9A-Fa-fK-Ok-oRr].*")) {
            return lcs.deserializeOrNull(s)
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        } else return mm.deserializeOrNull(s)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts component to a text
     * @param c a component to convert
     * @return a string
     */
    public static String parse(Component c) {
        return ptcs.serializeOrNull(c);
    }

    /**
     * Converts string to a Minecraft JSON string
     * @param s a string to convert
     * @return a component in form of JSON
     */
    public static Component gson(String s) {
        return gcs.deserializeOrNull(s)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts seconds into a human-readable time
     * @param t a time in seconds
     * @return a formatted time string
     */
    public static String time(double t) {
        int hours = (int) t / 3600;
        int minutes = (int) (t % 3600) / 60;
        int seconds = (int) t % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
