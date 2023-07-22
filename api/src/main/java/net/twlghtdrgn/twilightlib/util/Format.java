package net.twlghtdrgn.twilightlib.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Formatters used in my plugins
 * @author TwlghtDrgn
 * @version 0.0.1
 */
public class Format {
    private Format() {}

    /**
     * Converts text to a component
     * @param string a string to convert
     * @return a component
     */
    @NotNull
    public static Component parse(String string) {
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
    public static Component gson(String string) {
        return GsonComponentSerializer.gson().deserializeOr(string, Component.text("Error"))
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
