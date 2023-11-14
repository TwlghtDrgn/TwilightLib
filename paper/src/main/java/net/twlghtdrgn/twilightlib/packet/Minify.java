package net.twlghtdrgn.twilightlib.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.twlghtdrgn.twilightlib.LibraryPermission;
import net.twlghtdrgn.twilightlib.TwilightLib;
import net.twlghtdrgn.twilightlib.TwilightPlugin;
import net.twlghtdrgn.twilightlib.api.config.Configuration;
import net.twlghtdrgn.twilightlib.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

public class Minify {
    private final TwilightPlugin plugin;
    private ProtocolManager api;
    private Configuration<Config> cfg;
    public Minify(TwilightLib plugin) throws ConfigurateException {
        this.plugin = plugin;
        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) return;
        this.api = ProtocolLibrary.getProtocolManager();
        this.cfg = new Configuration<>(plugin, Config.class, "minify");
        cfg.reload();

        listenChat();
        listenActionbar();
        listenInventory();
        listenTitle();
        listenBossbar();
        listenScoreboard();

        listenAnvil();
    }

    private void listenChat() {
        if (!cfg.get().chat) return;
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final PacketContainer container = event.getPacket();
                final boolean isLegacyActionbar = container.getBooleans().read(0);
                Object unknown;
                if (isLegacyActionbar && cfg.get().actionBar) {
                    unknown = container.getModifier().read(1);
                    container.getModifier().write(1, ComponentUtil.convert(unknown));
                } else {
                    unknown = container.getModifier().read(0);
                    container.getModifier().write(0, ComponentUtil.convert(unknown));
                }
            }
        });
    }

    private void listenActionbar() {
        if (!cfg.get().actionBar) return;
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SET_ACTION_BAR_TEXT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final PacketContainer container = event.getPacket();
                Object unknown = container.getModifier().read(0);
                if (unknown == null) System.out.println("NULL!! 62");
                container.getModifier().write(0, ComponentUtil.convert(unknown));
            }
        });
    }

    private void listenInventory() {
        if (!cfg.get().inventory) return;
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.OPEN_WINDOW) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final PacketContainer container = event.getPacket();
                Object unknown = container.getModifier().read(2);
                if (unknown == null) System.out.println("NULL!! 76");
                container.getModifier().write(2, ComponentUtil.convert(unknown));
            }
        });
    }

    private void listenTitle() {
        if (!cfg.get().title) return;
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SET_TITLE_TEXT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                Object unknown = container.getModifier().read(0);
                if (unknown == null) System.out.println("NULL!! 89");
                container.getModifier().write(0, ComponentUtil.convert(unknown));
            }
        });
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SET_SUBTITLE_TEXT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                Object unknown = container.getModifier().read(0);
                if (unknown == null) System.out.println("NULL!! 98");
                container.getModifier().write(0, ComponentUtil.convert(unknown));
            }
        });
    }

    private void listenBossbar() {
        if (!cfg.get().bossbar) return; // TODO
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.BOSS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();

                for (Object o:container.getBooleans().getValues())
                    System.out.println(o);
            }
        });
    }

    private void listenScoreboard() {
        if (!cfg.get().scoreboard) return; // TODO

    }

    private void listenAnvil() {
        if (!cfg.get().disallowMinimessageAnvil) return;
        api.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.ITEM_NAME) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer container = event.getPacket();
                Player player = event.getPlayer();
                String name = container.getStrings().read(0);
                boolean containsBrackets = name.matches(".*<.*>.*");
                if (player.hasPermission(LibraryPermission.ANVIL_MINIMESSAGE_BRACKETS)) return;
                if (!containsBrackets) return;
                event.setCancelled(true);
            }
        });
    }

    @ConfigSerializable
    protected static class Config {
        private boolean chat = false;
        private boolean actionBar = false;
        private boolean inventory = false;
        private boolean title = false;
        private boolean bossbar = false; // TODO
        private boolean scoreboard = false; // TODO

        private boolean disallowMinimessageAnvil = true;
    }
}
