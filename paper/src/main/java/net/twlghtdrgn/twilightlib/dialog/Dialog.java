package net.twlghtdrgn.twilightlib.dialog;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.api.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Dialog<T extends DialogOwner> {
    private static final Set<DialogEntry> entries = new HashSet<>();
    private final String initQuestion;
    private final T dialogOwner;
    private final Set<Player> dialogUsers = new HashSet<>();
    public Dialog(@NotNull TwilightPlugin plugin, @NotNull T owner, @NotNull DialogEntry initEntry) {
        this.dialogOwner = owner;
        this.initQuestion = initEntry.getQuestion();
        entries.add(initEntry);
        Bukkit.getPluginManager().registerEvents(new DialogEvent(dialogOwner.getName()), plugin);
    }

    public void start(Player player) {
        Optional<DialogEntry> init = getEntry(initQuestion);
        if (init.isEmpty()) return;
        player.sendMessage(builtEntry(init.get()));
        dialogUsers.add(player);
    }

    public void addEntry(DialogEntry entry) {
        entries.add(entry);
    }

    public Optional<DialogEntry> getEntry(String question) {
        return entries.stream()
                .filter(entry -> entry.getQuestion().equals(question))
                .findFirst();
    }

    public Component builtEntry(@NotNull DialogEntry entry) {
        final StringJoiner joiner = new StringJoiner("\n");
        joiner.add(dialogOwner.getName() + ": " + entry.getQuestion());
        for (var val:entry.getAnswers().values()) {
            joiner.add("> <click:run" + val);
        }
        return Format.parse(joiner.toString());
    }
}
