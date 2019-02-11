package gg.vexus.listeners;

import java.util.Collection;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import gg.mist.hcf.HCF;
import gg.mist.hcf.faction.event.FactionChatEvent;
import gg.mist.hcf.faction.struct.ChatChannel;
import gg.mist.hcf.faction.type.PlayerFaction;
import gg.vexus.Core;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatListener implements Listener
{
    private final Core plugin;
    
    public ChatListener(final Core plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        final Player p = e.getPlayer();
        final PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(p);
        final ChatChannel chatChannel = (playerFaction == null) ? ChatChannel.PUBLIC : playerFaction.getMember(p).getChatChannel();
        final Set<Player> recipients = (Set<Player>)e.getRecipients();
        if (chatChannel == ChatChannel.FACTION || chatChannel == ChatChannel.ALLIANCE) {
            if (!this.isGlobalChannel(message)) {
                final Collection<Player> online = playerFaction.getOnlinePlayers();
                if (chatChannel == ChatChannel.ALLIANCE) {
                    final Collection<PlayerFaction> allies = playerFaction.getAlliedFactions();
                    for (final PlayerFaction ally : allies) {
                        online.addAll(ally.getOnlinePlayers());
                    }
                }
                recipients.retainAll(online);
                e.setFormat(chatChannel.getRawFormat(p));
                Bukkit.getPluginManager().callEvent((Event)new FactionChatEvent(true, playerFaction, p, chatChannel, (Collection<? extends CommandSender>)recipients, e.getMessage()));
                return;
            }
            message = message.substring(1, message.length()).trim();
            e.setMessage(message);
        }
        e.setCancelled(true);
        final String rank = ChatColor.translateAlternateColorCodes('&', "&e" + PermissionsEx.getUser(p).getPrefix()).replace("_", " ");
        String displayName = p.getDisplayName();
        displayName = String.valueOf(rank) + displayName;
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        String tag = (playerFaction == null) ? (ChatColor.DARK_GREEN + "*") : playerFaction.getDisplayName((CommandSender)console);
        console.sendMessage(ChatColor.GRAY + "[" + tag + ChatColor.GRAY + "] " + displayName + ChatColor.GOLD + " » " + ChatColor.WHITE + message);
        for (final Player recipient : e.getRecipients()) {
            tag = ((playerFaction == null) ? (ChatColor.RED + "*") : playerFaction.getDisplayName((CommandSender)recipient));
            recipient.sendMessage(ChatColor.GRAY + "[" + tag + ChatColor.GRAY + "] " + displayName + ChatColor.GOLD + " » " + ChatColor.WHITE + message);
        }
    }
    
    private boolean isGlobalChannel(final String input) {
        final int length = input.length();
        if (length <= 1 || !input.startsWith("!")) {
            return false;
        }
        int i = 1;
        while (i < length) {
            final char character = input.charAt(i);
            if (character == ' ') {
                ++i;
            }
            else {
                if (character == '/') {
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
