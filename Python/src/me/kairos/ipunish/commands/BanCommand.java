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

public class BanCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public BanCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "/" + label + " <player> [reason]");
            return true;
        }

        String playerName;
        Profile profile;
        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            playerName = target.getName();
            profile = plugin.getProfileManager().getProfile(target.getUniqueId());
        } else {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);

            if (!offlineTarget.hasPlayedBefore()) {
                sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                return true;
            }

            playerName = offlineTarget.getName();
            profile = plugin.getProfileManager().getProfile(offlineTarget.getUniqueId());
        }

        if (profile == null) {
            sender.sendMessage(Utils.getProfileNotFoundMessage(args[0]));
            return true;
        }

        if (profile.getBanRemaining() != Profile.PunishmentStatus.NONE.getId()) {
            sender.sendMessage(ChatColor.RED + "That player is already banned/tempbanned!");
            return true;
        }

        String reason = args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : plugin.getConfiguration().getDefaultBanReason();
        Utils.kickFromNetwork(playerName, plugin.getConfiguration().getBanMessage().replace("%REASON%", reason), sender, plugin);

        profile.setBannerUUID(IPunish.getUUID(sender));
        profile.setBanReason(reason);
        profile.incrementBanCount();
        profile.setBanDurationMillis(Profile.PunishmentStatus.PERMANENT.getId());
        profile.setBanStamp(System.currentTimeMillis());
        plugin.getProfileManager().save(profile);

        Bukkit.broadcastMessage(plugin.getConfiguration().getBanBroadcastMessage().replace("%BANNED_PLAYER%", playerName).replace("%PLAYER%", sender.getName()));
        Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Name: " + ChatColor.RED + playerName);
        Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Banned by: " + ChatColor.RED + sender.getName());
        Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Reason: " + ChatColor.RED + profile.getBanReason());
        if (profile.getBanRemaining() != Profile.PunishmentStatus.PERMANENT.getId()) {
            Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "Expires: " + ChatColor.RED +
                    DurationFormatUtils.formatDurationWords(profile.getBanRemaining(), true, true));
        }

        Utils.sendStaffMessage(ChatColor.DARK_RED + "* " + ChatColor.RED + "IPs: " + ChatColor.RED + profile.getIps().size());
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
