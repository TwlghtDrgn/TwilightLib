package net.twlghtdrgn.twilightlib.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@SuppressWarnings("unused")
@AllArgsConstructor
public class CallbackEvent extends Event implements Cancellable {
    public static final String CALLBACK_COMMAND = "twilightlib:callback";
    private static final HandlerList HANDLER_LIST = new HandlerList();

    /**
     * A player that triggered this event
     */
    private final Player player;

    /**
     * A plugin, which uses this callback
     */
    private final String plugin;

    /**
     * Callback channel, used for filtering data
     */
    private final String channel;

    /**
     * Callback data, which contains a payload for the plugin listening on this event
     */
    private final String[] data;
    private boolean cancelled;

    /**
     * Check if this is REALLY your data
     * @param plugin callback plugin
     * @param channel callback channel
     */
    public boolean isMyData(@NotNull TwilightPlugin plugin, String channel) {
        return this.plugin.equalsIgnoreCase(plugin.getName()) && this.channel.equalsIgnoreCase(channel);
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    public static class CallbackCommandBuilder {
        private TwilightPlugin plugin;
        private String channel;

        public CallbackCommandBuilder plugin(@NotNull TwilightPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public CallbackCommandBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public String build() {
            if (plugin == null || channel == null || channel.isEmpty())
                throw new IllegalArgumentException("You should set all builder values for the CallbackBuilder to work");
            return String.format("%s %s:%s", CALLBACK_COMMAND, plugin.getName(), channel);
        }
    }
}
