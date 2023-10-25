package net.twlghtdrgn.twilightlib.dialog;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.api.util.Format;
import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import net.twlghtdrgn.twilightlib.listener.DialogEventListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Dialog<T extends DialogOwner> {
    private final Set<DialogEntry> entries = new HashSet<>();
    private final String startPrompt;
    private final String endPrompt;
    private final T dialogOwner;
    private final Set<Player> dialogUsers = new HashSet<>();
    private final TwilightPlugin plugin;

    public Dialog(@NotNull TwilightPlugin plugin, @NotNull T owner, @NotNull DialogEntry initEntry, @NotNull String endPrompt) {
        this.plugin = plugin;
        this.dialogOwner = owner;
        this.startPrompt = initEntry.getPrompt();
        this.endPrompt = endPrompt;
        entries.add(initEntry);

        Bukkit.getPluginManager().registerEvents(new DialogEventListener(this, plugin), plugin);
    }

    public void start(Player player) {
        final Optional<DialogEntry> startEntry = getEntry(startPrompt);
        if (startEntry.isEmpty()) return;
        player.sendMessage(getBuiltEntry(startEntry.get()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 25, false, false));
        dialogUsers.add(player);
    }

    public void end(Player player) {
        dialogUsers.remove(player);
        player.removePotionEffect(PotionEffectType.SLOW);
        final Optional<DialogEntry> endEntry = getEntry(endPrompt);
        if (endEntry.isEmpty()) return;
        player.sendMessage(getBuiltEntry(endEntry.get()));
    }

    public void addEntry(DialogEntry entry) {
        entries.add(entry);
    }

    public Optional<DialogEntry> getEntry(String prompt) {
        return entries.stream()
                .filter(entry -> entry.getPrompt().equals(prompt))
                .findFirst();
    }

    public Component getBuiltEntry(@NotNull DialogEntry entry) {
        final StringJoiner joiner = new StringJoiner("\n");
        joiner.add(dialogOwner.getName() + ": " + entry.getPrompt());
        for (var val:entry.getAnswers().entrySet()) {
            joiner.add(String.format("> <click:run_command:'/%s %s'>%s",
                            ChatCallbackEvent.getCallbackPrefix(plugin, dialogOwner.getName()),
                            val.getValue(),
                            val.getKey()));
        }
        return Format.parse(joiner.toString());
    }

    public Optional<DialogEntry> getCallbackEntry(@NotNull String entryID) {
        return entries.stream()
                .filter(dialogEntry -> dialogEntry.getEntryID().equals(entryID))
                .findFirst();
    }
}
