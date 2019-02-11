package io.hopt.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.bizarrealex.aether.scoreboard.cooldown.BoardFormat;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.hopt.Nexus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.*;

/**
 * Created by Owner on 19/06/2017.
 */
public class NexusAdapter implements BoardAdapter, Listener {

    public static int Online = 1;
    private Map<String, Integer> ranks;
    private static final String GROUP_PREFIX = "group.";

    @Override
    public String getTitle(Player player) {
        return ChatColor.translateAlternateColorCodes('&', Nexus.config.getString("SCOREBOARD.TITLE"));
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
        List<String> strings = new ArrayList();

        getCount("ALL");
        
        strings.add("&7&m-------------------");
        strings.add(ChatColor.translateAlternateColorCodes('&', "&b&lOnline Players:"));
        strings.add(ChatColor.translateAlternateColorCodes('&', "&7" + Online));
        strings.add(" ");
        strings.add(ChatColor.translateAlternateColorCodes('&', "&b&lRank:"));
        strings.add(ChatColor.translateAlternateColorCodes('&', "&7" + getGroupsForPlayer(player)));
        strings.add(" ");
        strings.add("&b&lStore:");
        strings.add(ChatColor.translateAlternateColorCodes('&', "&7www.vexus.gg"));
        for (BoardCooldown cooldown : cooldowns) {
            if (cooldown.getId().equals("profile")) {
                strings.add("&A&lProfile Sync: " + cooldown.getFormattedString(BoardFormat.SECONDS));
            }
        }
        strings.add("&7&m-------------------&r");
        if (strings.size() == 2) {
            return null;
        }
        return strings;
    }

    public void getCount(String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(server);

            Bukkit.getServer().sendPluginMessage((Plugin) this, "BungeeCord", out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String command = in.readUTF();

            if (command.equals("PlayerCount")) {
                String subchannel = in.readUTF();
                if (subchannel.equals("ALL")) {
                    int playercount = in.readInt();
                    Online = playercount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getGroupsForPlayer(Player player) {
        Set<String> groups = new HashSet<String>();
        for (PermissionAttachmentInfo pai : player.getEffectivePermissions()) {
            if (!pai.getPermission().startsWith(GROUP_PREFIX) || !pai.getValue())
                continue;
            groups.add(pai.getPermission().substring(GROUP_PREFIX.length()));
        }
        return groups;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Board board = Board.getByPlayer(player);
        BoardCooldown cooldown = board.getCooldown("profile");

        if (cooldown != null) {
            return;
        }
        new BoardCooldown(board, "profile", 10.0);
    }
}
