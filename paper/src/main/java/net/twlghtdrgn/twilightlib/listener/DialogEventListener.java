package net.twlghtdrgn.twilightlib.listener;

import net.kyori.adventure.text.Component;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.dialog.Dialog;
import net.twlghtdrgn.twilightlib.dialog.DialogEntry;
import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class DialogEventListener implements Listener {
    private final Dialog<?> dialog;
    private final TwilightPlugin plugin;
    public DialogEventListener(Dialog<?> dialog, TwilightPlugin plugin) {
        this.dialog = dialog;
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatCallback(@NotNull ChatCallbackEvent event) {
        if (!event.getPlugin().equalsIgnoreCase(plugin.getName()) || !event.getChannel().equalsIgnoreCase(dialog.getDialogOwner().getName())) return;
        final Player player = event.getPlayer();
        if (event.getData()[0].equals(dialog.getEndPrompt())) {
            dialog.end(player);
            event.setCancelled(true);
            return;
        }
        if (event.getData().length < 2) return;

        if (event.getData()[0].equals("dialog_next")) {
            Optional<DialogEntry> entry = dialog.getCallbackEntry(event.getData()[1]);
            if (entry.isEmpty()) return;
            final Component component = dialog.getBuiltEntry(entry.get());
            player.sendMessage(component);
        }
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (dialog.getDialogUsers().contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSneak(@NotNull PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (dialog.getDialogUsers().contains(player)) {
            event.setCancelled(true);
            dialog.end(player);
        }
    }

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (dialog.getDialogUsers().contains(player)) {
            event.setCancelled(true);
        }
    }
}
