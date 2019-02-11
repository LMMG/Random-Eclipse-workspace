package me.kairos.ipunish.commands;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import me.kairos.ipunish.profiles.Profile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TempbanCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public TempbanCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            long banDurationMillis = Utils.parseMillis(args[1]);

            if (banDurationMillis <= 0) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid time!");
                sender.sendMessage(ChatColor.RED + "Example: /" + label + " " + args[0] + " 5m");
                return true;
            }

            banDurationMillis *= 1000L;

            Profile profile;
            String name;
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target != null) {
                name = target.getName();
                profile = plugin.getProfileManager().getProfile(target.getUniqueId());
            } else {
                OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);

                if (!offlineTarget.hasPlayedBefore()) {
                    sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                    return true;
                }

                name = offlineTarget.getName();
                profile = plugin.getProfileManager().getProfile(offlineTarget.getUniqueId());
            }

            if (profile == null) {
                sender.sendMessage(Utils.getProfileNotFoundMessage(args[0]));
                return true;
            }

            if (profile.getBanRemaining() == Profile.PunishmentStatus.PERMANENT.getId()) {
                sender.sendMessage(ChatColor.RED + "That player is already permanently banned!");
                return true;
            }

            if (profile.getBanRemaining() != Profile.PunishmentStatus.NONE.getId()) {
                sender.sendMessage(ChatColor.RED + "That player is already tempbanned, you have overwritten their tempban length.");
            }

            String remainingWords = DurationFormatUtils.formatDurationWords(banDurationMillis, true, true);
            String reason = args.length > 2 ? StringUtils.join(args, ' ', 2, args.length) : plugin.getConfiguration().getDefaultBanReason();
            Utils.kickFromNetwork(name, plugin.getConfiguration().getTempbanMessage().replace("%REASON%", reason).replace("%TIME%", remainingWords), sender, plugin);

            profile.incrementBanCount();
            profile.setBannerUUID(IPunish.getUUID(sender));
            profile.setBanReason(reason);
            profile.incrementBanCount();
            profile.setBanStamp(System.currentTimeMillis());
            profile.setBanDurationMillis(banDurationMillis);
            plugin.getProfileManager().save(profile);

            Bukkit.broadcastMessage(plugin.getConfiguration().getTempbanBroadcast().replace("%BANNED_PLAYER%", name).replace("%PLAYER%", sender.getName()).replace("%TIME%", remainingWords));
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Name: " + ChatColor.RED + name);
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Banned by: " + ChatColor.RED + sender.getName());
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Reason: " + ChatColor.RED + profile.getBanReason());
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Expires: " + ChatColor.RED + remainingWords);
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "IPs: " + ChatColor.RED + profile.getIps().size());
            return true;
        }

        sender.sendMessage(ChatColor.RED + "/" + label + " <playerName> <duration> [reason]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return null;
        } else if (args.length == 2) {
            return Utils.getCompletions(args, new ArrayList<>(plugin.getConfiguration().getBanReasons()));
        } else {
            return Collections.emptyList();
        }
    }
}
