package me.kairos.ipunish.commands;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import me.kairos.ipunish.profiles.Profile;
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

public class UnmuteCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public UnmuteCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
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
                sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                return true;
            }

            if (profile.getMuteRemaining() == Profile.PunishmentStatus.NONE.getId()) {
                sender.sendMessage(ChatColor.RED + "That player is not muted!");
                return true;
            }

            profile.setMuteReason(null);
            profile.setMuterUUID(null);
            profile.setMuteStamp(Profile.PunishmentStatus.NONE.getId());
            profile.setMuteDurationMillis(Profile.PunishmentStatus.NONE.getId());
            plugin.getProfileManager().save(profile);

            Bukkit.broadcastMessage(plugin.getConfiguration().getUnmuteBroadcast().replace("%UNMUTED_PLAYER%", name).replace("%PLAYER%", sender.getName()));
            return true;
        }

        sender.sendMessage(ChatColor.RED + "/" + label + " <playerName>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}
