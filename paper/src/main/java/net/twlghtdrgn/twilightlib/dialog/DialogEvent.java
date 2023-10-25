package net.twlghtdrgn.twilightlib.dialog;

import lombok.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

@Data
public class DialogEvent implements Listener {

    private static final String COMMAND_PREFIX = "twilightlib:dialog-engine:callback:";
    private final String commandID;
    public DialogEvent(String commandID) {
        this.commandID = commandID;
    }

    @EventHandler
    public void onDialogCommandEvent(@NotNull PlayerCommandPreprocessEvent event) {
        if (!event.getMessage().startsWith(COMMAND_PREFIX + commandID)) return;
        event.setCancelled(true);
        final String action = event.getMessage().split(commandID + " ")[1];
    }
}
