package net.twlghtdrgn.twilightlib.event;

import lombok.Getter;
import lombok.Setter;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ChatCallbackEvent extends Event implements Listener, Cancellable {
    private static final String COMMAND_PREFIX = "twilightlib:callback";
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private Player player;
    private String plugin;
    private String channel;
    private String[] data;
    private boolean cancelled;

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @EventHandler
    public void onSlashCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/" + COMMAND_PREFIX)) return;
        event.setCancelled(true);

        final String[] in = event.getMessage().split(" ");
        if (in.length < 3) throw new IllegalArgumentException(String.format("Not enough callback data (%s)", in.length));
        else if (in.length > 3) throw new IllegalArgumentException(String.format("Oversized callback data (%s)", in.length));

        final String[] meta = in[1].split(":");
        if (meta.length < 2) throw new IllegalArgumentException(String.format("Not enough channel data (%s)", meta.length));
        else if (meta.length > 2) throw new IllegalArgumentException(String.format("Oversized channel data (%s)", in.length));

        this.player = event.getPlayer();
        this.plugin = meta[0];
        this.channel = meta[1];
        this.data = in[2].split(":");

        this.callEvent();
    }

    public static String getCallbackPrefix(@NotNull TwilightPlugin plugin, String channel){
        return String.format("%s %s:%s", COMMAND_PREFIX, plugin.getName(), channel);
    }
}
