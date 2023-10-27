package net.twlghtdrgn.twilightlib.dialog;

import lombok.Data;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.api.config.Configuration;
import net.twlghtdrgn.twilightlib.api.util.Format;
import net.twlghtdrgn.twilightlib.event.ChatCallbackEvent;
import net.twlghtdrgn.twilightlib.listener.DialogEventListener;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@Getter
public class Dialog<T extends DialogOwner> {
    private final Set<DialogEntry> entries = new HashSet<>();
    private final String startEntryID;
    private final String endEntryID;
    private final T dialogOwner;
    private final Set<Player> dialogUsers = new HashSet<>();
    private final Configuration<DialogConfig> conf;
    private final TwilightPlugin plugin;

    public Dialog(@NotNull TwilightPlugin plugin, @NotNull T owner, @NotNull DialogEntry initEntry, @NotNull String endEntryID) {
        this.plugin = plugin;
        this.conf = new Configuration<>(plugin, DialogConfig.class, "dialog");

        try {
            conf.reload();
        } catch (ConfigurateException e) {
            plugin.log().error("Unable to load configuration for dialog '{}': {}", getDialogOwner().getName(), e.getMessage(), e);
            throw new NullPointerException("Unable to load a dialog config: " + e.getMessage());
        }

        this.dialogOwner = owner;
        this.startEntryID = initEntry.getEntryID();
        this.endEntryID = endEntryID;
        this.entries.add(initEntry);

        Bukkit.getPluginManager().registerEvents(new DialogEventListener(this, plugin), plugin);
    }

    public void start(Player player) {
        final Optional<DialogEntry> startEntry = getEntry(startEntryID);
        if (startEntry.isEmpty()) return;
        player.sendMessage(getBuiltEntry(startEntry.get()));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 25, false, false));
        dialogUsers.add(player);
    }

    public void end(Player player) {
        dialogUsers.remove(player);
        player.removePotionEffect(PotionEffectType.SLOW);
        final Optional<DialogEntry> endEntry = getEntry(endEntryID);
        if (endEntry.isEmpty()) return;
        player.sendMessage(getBuiltEntry(endEntry.get()));
    }

    public Dialog<T> addEntry(DialogEntry entry) {
        entries.add(entry);
        return this;
    }

    public Component getBuiltEntry(@NotNull DialogEntry entry) {
        final StringJoiner joiner = new StringJoiner("\n");
        if (conf.get().isClearChatEnabled())
            joiner.add(StringUtils.repeat(" \n", 10));
        joiner.add(conf.get().getHeader()
                .replace("{NAME}", dialogOwner.getName()));
        joiner.add(getQuestion(entry.getPrompt()));
        for (var val:entry.getAnswers().entrySet()) {
            joiner.add(getOption(val));
        }
        joiner.add("");
        return Format.parse(joiner.toString());
    }

    public Optional<DialogEntry> getEntry(@NotNull String entryID) {
        return entries.stream()
                .filter(dialogEntry -> dialogEntry.getEntryID().equals(entryID))
                .findFirst();
    }

    @Data
    @ConfigSerializable
    protected static class DialogConfig {
        @Setting("clear-chat")
        private boolean clearChatEnabled = true;
        private String header = "<yellow><bold>{NAME}";
        private String question = "<gold>{TEXT_ROW}<reset>";
        private int questionRows = 4;

        private String option = "> <click:run_command:'{ACTION}'>{TEXT}<reset>";
    }

    private String getOption(Map.@NotNull Entry<String, String> entry) {
        return String.format(conf.get().getOption()
                .replace("{ACTION}",
                        String.format("/%s %s",
                                ChatCallbackEvent.getCallbackCommand(plugin, dialogOwner.getName()),
                                entry.getValue()))
                .replace("{TEXT}", entry.getKey()));
    }

    public String getQuestion(@NotNull String question) {
        final String[] split = question.split("<newline>");
        final StringJoiner sj = new StringJoiner("\n");
        for (int i = 0; i < conf.get().getQuestionRows(); i++) {
            final String row = conf.get().getQuestion()
                    .replace("{TEXT_ROW}", i > split.length ? "" : split[i]);
            sj.add(row);
        }
        return sj.toString();
    }
}
