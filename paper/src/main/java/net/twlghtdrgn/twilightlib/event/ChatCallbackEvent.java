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
    private static final String CALLBACK_COMMAND = "twilightlib:callback";
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
        return getHandlerList();
    }

    @EventHandler
    public void onSlashCommand(@NotNull PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith("/" + CALLBACK_COMMAND)) return;
        final String[] in = event.getMessage().split(" ");
        if (in.length != 3) return;
        final String[] meta = in[1].split(":");
        if (meta.length != 2) return;
        event.setCancelled(true);


        this.player = event.getPlayer();
        this.plugin = meta[0];
        this.channel = meta[1];
        this.data = in[2].split(":");

        this.callEvent();
    }

    public static String getCallbackCommand(@NotNull TwilightPlugin plugin, String channel){
        return String.format("%s %s:%s", CALLBACK_COMMAND, plugin.getName(), channel);
    }
}
