package net.twlghtdrgn.twilightlib.listener;

import net.twlghtdrgn.twilightlib.event.CallbackEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public class CallbackListener implements Listener {
    @EventHandler
    public void onSlashCommand(@NotNull PlayerCommandPreprocessEvent event) {
        final String prefix = "/" + CallbackEvent.CALLBACK_COMMAND;
        if (!event.getMessage().startsWith(prefix)) return;
        final String command = event.getMessage().replaceFirst(prefix + " ","");
        final String[] content = command.split(":");

        if (content.length != 3) return;
        final CallbackEvent callbackEvent = new CallbackEvent(event.getPlayer(), content[0], content[1], content[2],false);
        event.setCancelled(true);
        callbackEvent.callEvent();
    }
}
