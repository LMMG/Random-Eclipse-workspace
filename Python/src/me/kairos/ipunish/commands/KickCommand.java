package me.kairos.ipunish.commands;

import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import me.kairos.ipunish.profiles.Profile;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class KickCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public KickCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(Utils.getPlayerNotFoundMessage(args[0]));
                return true;
            }

            target.kickPlayer(plugin.getConfiguration().getKickMessage().replace("%REASON%", args.length > 1 ? StringUtils.join(args, ' ', 1, args.length) : plugin.getConfiguration().getDefaultBanReason()));

            Profile profile = plugin.getProfileManager().getProfile(target.getUniqueId());
            profile.incrementKickCount();
            plugin.getProfileManager().save(profile);

            Bukkit.broadcastMessage(plugin.getConfiguration().getKickBroadcast().replace("%KICKED_PLAYER%", target.getName()).replace("%PLAYER%", sender.getName()));
            return true;
        }

        sender.sendMessage(ChatColor.RED + "/" + label + " <player> [reason]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}
