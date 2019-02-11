package com.parapvp.base.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permissible;

import com.google.common.collect.Sets;
import com.parapvp.base.BasePlugin;
import com.parapvp.base.event.PlayerMessageEvent;
import com.parapvp.base.user.BaseUser;
import com.parapvp.base.user.ServerParticipator;
import com.parapvp.util.BukkitUtils;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatListener implements Listener
{
    private static final String MESSAGE_SPY_FORMAT;
    private static final String STAFF_CHAT_FORMAT = "%1$s: %2$s";
    private static final String STAFF_CHAT_NOTIFY = "base.command.staffchat";
    private static final String SLOWED_CHAT_BYPASS = "base.slowchat.bypass";
    private static final String TOGGLED_CHAT_BYPASS = "base.disablechat.bypass";
    private static final long AUTO_IDLE_TIME;
    private final BasePlugin plugin;
    
    public ChatListener(final BasePlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String name = player.getName();
        final BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
        final Iterator<Player> iterator = event.getRecipients().iterator();
        while (iterator.hasNext()) {
            final Player target = iterator.next();
            final BaseUser targetUser = this.plugin.getUserManager().getUser(target.getUniqueId());
            if (baseUser.isInStaffChat() && !targetUser.isStaffChatVisible()) {
                iterator.remove();
            }
            else if (targetUser.getIgnoring().contains(name)) {
                iterator.remove();
            }
            else {
                if (targetUser.isGlobalChatVisible()) {
                    continue;
                }
                iterator.remove();
            }
        }
        if (baseUser.isInStaffChat()) {
            final Set<CommandSender> staffChattable = (Set)Sets.newHashSet();
            for (final Permissible permissible : Bukkit.getOnlinePlayers()) {
                if (permissible.hasPermission("base.command.staffchat") && permissible instanceof CommandSender) {
                    staffChattable.add((CommandSender)permissible);
                }
            }
            if (staffChattable.contains(player) && baseUser.isInStaffChat()) {
                final String format = ChatColor.LIGHT_PURPLE + "(Staff Chat) " + ChatColor.AQUA + String.format(Locale.ENGLISH, "%1$s" + ChatColor.GRAY + " %2$s", player.getName(), event.getMessage());
                for (final CommandSender target2 : staffChattable) {
                    if (target2 instanceof Player) {
                        final Player targetPlayer = (Player)target2;
                        final BaseUser targetUser2 = this.plugin.getUserManager().getUser(targetPlayer.getUniqueId());
                        if (targetUser2.isStaffChatVisible()) {
                            target2.sendMessage(format);
                        }
                        else {
                            if (!target2.equals(player)) {
                                continue;
                            }
                            target2.sendMessage(ChatColor.RED + "Your message was sent, but you cannot see staff chat messages as your notifications are disabled: Use /togglesc.");
                        }
                    }
                }
                event.setCancelled(true);
                return;
            }
        }
        final long remainingChatDisabled = this.plugin.getServerHandler().getRemainingChatDisabledMillis();
        if (remainingChatDisabled > 0L && !player.hasPermission("base.disablechat.bypass")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Global chat is currently disabled for another " + ChatColor.RED + DurationFormatUtils.formatDurationWords(remainingChatDisabled, true, true) + ChatColor.RED + '.');
            return;
        }
        final long remainingChatSlowed = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
        if (remainingChatSlowed > 0L && !player.hasPermission("base.slowchat.bypass")) {
            final long speakTimeRemaining = baseUser.getLastSpeakTimeRemaining();
            if (speakTimeRemaining <= 0L) {
                baseUser.updateLastSpeakTime();
                return;
            }
            event.setCancelled(true);
            final long delayMillis = this.plugin.getServerHandler().getChatSlowedDelay() * 1000L;
            player.sendMessage(ChatColor.RED + "Global chat is currently in slow mode with a " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(delayMillis, true, true) + ChatColor.RED + " delay for another " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(remainingChatSlowed, true, true) + ChatColor.RED + ". You spoke " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(delayMillis - speakTimeRemaining, true, true) + ChatColor.RED + " ago, so you have to wait another " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(speakTimeRemaining, true, true) + ChatColor.RED + '.');
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerPreMessage(final PlayerMessageEvent event) {
        final CommandSender sender = (CommandSender)event.getSender();
        final Player recipient = event.getRecipient();
        final UUID recipientUUID = recipient.getUniqueId();
        if (!sender.hasPermission("base.messaging.bypass")) {
            final BaseUser recipientUser = this.plugin.getUserManager().getUser(recipientUUID);
            if (!recipientUser.isMessagesVisible() || recipientUser.getIgnoring().contains(sender.getName())) {
                event.setCancelled(true);
                sender.sendMessage(ChatColor.RED + recipient.getName() + " has private messaging toggled.");
            }
            return;
        }
        final ServerParticipator senderParticipator = this.plugin.getUserManager().getParticipator(sender);
        if (!senderParticipator.isMessagesVisible()) {
            event.setCancelled(true);
            sender.sendMessage(ChatColor.RED + "You have private messages toggled.");
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMessage(final PlayerMessageEvent event) {
        final Player sender = event.getSender();
        final Player recipient = event.getRecipient();
        final String message = event.getMessage();
        if (BukkitUtils.getIdleTime(recipient) > ChatListener.AUTO_IDLE_TIME) {
            sender.sendMessage(ChatColor.RED + recipient.getName() + " may not respond as their idle time is over " + DurationFormatUtils.formatDurationWords(ChatListener.AUTO_IDLE_TIME, true, true) + '.');
        }
        final UUID senderUUID = sender.getUniqueId();
        final String senderId = senderUUID.toString();
        final String recipientId = recipient.getUniqueId().toString();
        final Collection<CommandSender> recipients = new HashSet<CommandSender>();
        recipients.remove(sender);
        recipients.remove(recipient);
        recipients.add((CommandSender)Bukkit.getConsoleSender());
        for (final CommandSender target : recipients) {
            final ServerParticipator participator = this.plugin.getUserManager().getParticipator(target);
            final Set<String> messageSpying = participator.getMessageSpying();
            if (messageSpying.contains("all") || messageSpying.contains(recipientId) || messageSpying.contains(senderId)) {
                target.sendMessage(String.format(Locale.ENGLISH, ChatListener.MESSAGE_SPY_FORMAT, sender.getName(), recipient.getName(), message));
            }
        }
    }

    	@EventHandler(priority = EventPriority.HIGHEST)
    	   public void chat(AsyncPlayerChatEvent e) {
    	   Player player = e.getPlayer();
    	   PermissionUser u = PermissionsEx.getUser(player);
    	   PermissionGroup g = u.getGroups()[0];
    	   if(g.getName().equalsIgnoreCase("OWNER")) {
    		   e.setMessage(ChatColor.RED + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("DEV")) {
    		   e.setMessage(ChatColor.AQUA + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("PLATFORM ADMIN")) {
    		   e.setMessage(ChatColor.RED + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("SRADMIN")) {
    		   e.setMessage(ChatColor.GRAY + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("ADMIN")) {
    		   e.setMessage(ChatColor.GRAY + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("SRMOD")) {
    		   e.setMessage(ChatColor.GRAY + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("MOD")) {
    		   e.setMessage(ChatColor.GRAY + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("TRIAL MOD")) {
    		   e.setMessage(ChatColor.GRAY + e.getMessage());
    	   } else if(g.getName().equalsIgnoreCase("MANAGER")) {
    		   e.setMessage(ChatColor.LIGHT_PURPLE + e.getMessage());
    	   }
    	}
    
    static {
        MESSAGE_SPY_FORMAT = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "SS: " + ChatColor.YELLOW + "%1$s" + ChatColor.WHITE + " -> " + ChatColor.YELLOW + "%2$s" + ChatColor.GOLD + "] %3$s";
        AUTO_IDLE_TIME = TimeUnit.MINUTES.toMillis(5L);
    }
}