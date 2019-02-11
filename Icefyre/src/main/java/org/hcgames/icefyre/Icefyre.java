package org.hcgames.icefyre;

import com.bizarrealex.aether.Aether;
import com.comphenix.tinyprotocol.Reflection;
import com.comphenix.tinyprotocol.TinyProtocol;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.arena.ArenaCommand;
import org.hcgames.icefyre.game.fight.FightHandler;
import org.hcgames.icefyre.game.fight.FightListeners;
import org.hcgames.icefyre.game.fight.duel.DuelCommand;
import org.hcgames.icefyre.game.fight.duel.DuelListeners;
import org.hcgames.icefyre.game.fight.inventory.FightInventoryCommand;
import org.hcgames.icefyre.game.kit.builder.KitBuilderListeners;
import org.hcgames.icefyre.game.ladder.LadderCommand;
import org.hcgames.icefyre.game.ladder.LadderHandler;
import org.hcgames.icefyre.game.queue.QueueHandler;
import org.hcgames.icefyre.game.queue.QueueListeners;
import org.hcgames.icefyre.lobby.Lobby;
import org.hcgames.icefyre.lobby.LobbyCommand;
import org.hcgames.icefyre.lobby.LobbyListeners;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.party.command.PartyCommand;
import org.hcgames.icefyre.party.fight.PartyFightListeners;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileListeners;
import org.hcgames.icefyre.party.PartyHandler;
import org.hcgames.icefyre.party.PartyListeners;
import org.hcgames.icefyre.util.IcefyreBoardAdapter;
import org.hcgames.icefyre.util.IcefyreDatabase;
import org.hcgames.icefyre.util.command.CommandFramework;
import org.hcgames.icefyre.util.file.ConfigFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class Icefyre extends JavaPlugin {

    private static Icefyre instance;

    @Getter private CommandFramework commandFramework;
    @Getter private ConfigFile configFile, langFile, ladderFile, arenaFile;
    @Getter private Aether aether;
    @Getter private Lobby lobby;
    @Getter private PartyHandler partyHandler;
    @Getter private LadderHandler ladderHandler;
    @Getter private QueueHandler queueHandler;
    @Getter private FightHandler fightHandler;
    @Getter private TinyProtocol tinyProtocol;
    @Getter private IcefyreDatabase icefyreDatabase;

    @Override
    public void onEnable() {
        instance = this;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        commandFramework = new CommandFramework(this);
        configFile = new ConfigFile(this, "config");
        langFile = new ConfigFile(this, "lang", false);
        arenaFile = new ConfigFile(this, "arenas", false);
        ladderFile = new ConfigFile(this, "ladders", false);
        aether = new Aether(this, new IcefyreBoardAdapter(this));
        icefyreDatabase = new IcefyreDatabase(this);

        Arena.load(this);
        Party.run(this);

        partyHandler = new PartyHandler(this);
        ladderHandler = new LadderHandler(this);
        queueHandler = new QueueHandler(this);
        fightHandler = new FightHandler(this);
        lobby = new Lobby(this);

        registerListeners();
        registerCommands();

        Profile.setup();

        tinyProtocol = new TinyProtocol(this) {
            @Override
            public Object onPacketInAsync(Player player, Channel channel, Object packet) {
                if (packet instanceof PacketPlayInTabComplete) {
                    Profile profile = Profile.getByPlayer(player);
                    if (profile != null) {
                        PacketPlayInTabComplete packetPlayInTabComplete = (PacketPlayInTabComplete) packet;
                        String message = Reflection.getField(packetPlayInTabComplete.getClass(), "a", String.class).get(packet);
                        String[] parts = message.split(" ");

                        if (message.endsWith(" ")) {
                            String[] newParts = new String[parts.length + 1];
                            for (int i = 0; i < parts.length; i++) {
                                newParts[i] = parts[i];
                            }
                            newParts[parts.length] = " ";
                            parts = newParts;
                        }

                        profile.setChatToken(parts[parts.length - 1]);

                    }
                }
                return super.onPacketInAsync(player, channel, packet);
            }

            @Override
            public Object onPacketOutAsync(Player player, Channel channel, Object packet) {
                if (packet instanceof PacketPlayOutTabComplete) {
                    Profile profile = Profile.getByPlayer(player);
                    if (profile != null) {
                        PacketPlayOutTabComplete packetPlayOutTabComplete = (PacketPlayOutTabComplete) packet;
                        List<String> response = new ArrayList<>(Arrays.asList(Reflection.getField(packetPlayOutTabComplete.getClass(), "a", String[].class).get(packet)));

                        for (Player online : Bukkit.getOnlinePlayers()) {
                            if (!(response.contains(online.getName()))) {
                                if (profile.getChatToken() == null || profile.getChatToken().replace(" ", "").isEmpty() || online.getName().toLowerCase().startsWith(profile.getChatToken().toLowerCase())) {
                                    response.add(online.getName());
                                }
                            }
                        }

                        Reflection.getField(packetPlayOutTabComplete.getClass(), "a", String[].class).set(packetPlayOutTabComplete, response.toArray(new String[response.size()]));
                    }
                }
                if (packet instanceof PacketPlayOutWorldEvent) {
                    Profile profile = Profile.getByPlayer(player);
                    if (profile != null) {
                        PacketPlayOutWorldEvent packetPlayOutWorldEvent = (PacketPlayOutWorldEvent) packet;
                        int id = Reflection.getField(packetPlayOutWorldEvent.getClass(), "a", int.class).get(packet);
                        int x = Reflection.getField(packetPlayOutWorldEvent.getClass(), "c", int.class).get(packet);
                        int y = Reflection.getField(packetPlayOutWorldEvent.getClass(), "d", int.class).get(packet);
                        int z = Reflection.getField(packetPlayOutWorldEvent.getClass(), "e", int.class).get(packet);
                        if (id == 2002) {

                            Iterator<Location> iterator = profile.getPotions().iterator();
                            while (iterator.hasNext()) {
                                Location location = iterator.next();
                                if (Math.rint(location.getX()) == x && Math.rint(location.getY()) == y && Math.rint(location.getZ()) == z) {
                                    iterator.remove();
                                    return super.onPacketOutAsync(player, channel, packet);
                                }
                            }

                            return null;
                        }
                    }
                }

                if (packet instanceof PacketPlayOutNamedSoundEffect) {
                    Profile profile = Profile.getByPlayer(player);
                    PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = (PacketPlayOutNamedSoundEffect) packet;
                    if (profile != null) {
                        String name = Reflection.getField(packetPlayOutNamedSoundEffect.getClass(), "a", String.class).get(packet);

                        if (name.equals("random.bowhit") && Reflection.getField(packetPlayOutNamedSoundEffect.getClass(), "e", float.class).get(packet) == 1.0) {
                            return null;
                        }

                        if (name.equals("random.bow")) {

                            if (!profile.getPackets().contains(packetPlayOutNamedSoundEffect)) {
                                return null;
                            }

                            return super.onPacketOutAsync(player, channel, packet);
                        }
                    }

                }
                return super.onPacketOutAsync(player, channel, packet);
            }
        };
    }

    @Override
    public void onDisable() {
        lobby.save();
        ladderHandler.save();
        Arena.save(this); //TODO: Convert to a handler or something?
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new LobbyListeners(lobby), this);
        pluginManager.registerEvents(new ProfileListeners(), this);
        pluginManager.registerEvents(new PartyListeners(), this);
        pluginManager.registerEvents(new QueueListeners(), this);
        pluginManager.registerEvents(new FightListeners(), this);
        pluginManager.registerEvents(new KitBuilderListeners(), this);
        pluginManager.registerEvents(new DuelListeners(), this);
        pluginManager.registerEvents(new PartyFightListeners(), this);
    }

    private void registerCommands() {
        new LobbyCommand();
        new LadderCommand();
        new ArenaCommand();
        new FightInventoryCommand();
        new DuelCommand();
        new PartyCommand();
    }

    public static Icefyre getInstance() {
        return instance;
    }

    public FightHandler getFightHandler() {
        return fightHandler;
    }
}
