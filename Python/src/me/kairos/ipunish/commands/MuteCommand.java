package me.kairos.ipunish.commands;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import me.kairos.ipunish.profiles.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MuteCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public MuteCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            long muteDurationMillis = Utils.parseMillis(args[1]);

            if (muteDurationMillis <= 0L) {
                sender.sendMessage(ChatColor.RED + "Please enter a valid time!");
                sender.sendMessage(ChatColor.RED + "Example: /" + label + " " + args[0] + " 5m");
                return true;
            }

            muteDurationMillis *= 1000L;

            String playerName;
            Profile profile;
            Player target = Bukkit.getPlayerExact(args[0]);
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

            if (profile.getMuteRemaining() != Profile.PunishmentStatus.NONE.getId()) {
                sender.sendMessage(ChatColor.RED + "That player is muted!");
                return true;
            }

            profile.setMuterUUID(IPunish.getUUID(sender));
            profile.setMuteReason(args.length > 2 ? StringUtils.join(args, ' ', 2, args.length) : plugin.getConfiguration().getDefaultBanReason());
            profile.incrementMuteCount();
            profile.setMuteStamp(System.currentTimeMillis());
            profile.setMuteDurationMillis(muteDurationMillis);
            plugin.getProfileManager().save(profile);

            Bukkit.broadcastMessage(plugin.getConfiguration().getMuteBroadcast().replace("%MUTED_PLAYER%", playerName).replace("%PLAYER%", sender.getName()));
            return true;
        }

        sender.sendMessage(ChatColor.RED + "/" + label + " (player) [time]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}
