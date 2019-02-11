package server.wenjapvp.hcf.deathban.lives;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.doctordark.util.BukkitUtils;
import com.doctordark.util.command.ArgumentExecutor;
import com.doctordark.util.command.CommandArgument;

import server.wenjapvp.hcf.HCF;
import server.wenjapvp.hcf.deathban.lives.argument.*;

/**
 * Handles the execution and tab completion of the lives command.
 */
public class LivesExecutor extends ArgumentExecutor {

    public LivesExecutor(HCF plugin) {
        super("lives");

        addArgument(new LivesCheckArgument(plugin));
        addArgument(new LivesCheckDeathbanArgument(plugin));
        addArgument(new LivesClearDeathbansArgument(plugin));
        addArgument(new LivesGiveArgument(plugin));
        addArgument(new LivesReviveArgument(plugin));
        addArgument(new LivesSetArgument(plugin));
        addArgument(new LivesSetDeathbanTimeArgument());
        addArgument(new LivesTopArgument(plugin));
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
        	sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        	sender.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Lives");
            sender.sendMessage(ChatColor.DARK_GREEN + "/lives check" + ChatColor.YELLOW + " - " + ChatColor.GRAY + "Check your lives.");
            sender.sendMessage(ChatColor.DARK_GREEN + "/lives revive <player>" + ChatColor.YELLOW + " - " + ChatColor.GRAY + "Revive a player.");
            sender.sendMessage(ChatColor.DARK_GREEN + "/lives give <player>" + ChatColor.YELLOW + " - " + ChatColor.GRAY + "Gives a player.");
            sender.sendMessage(ChatColor.DARK_GREEN + "/lives top" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Top players with the most lives.");
            sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);

            return true;
        }
        final CommandArgument argument2 = this.getArgument(args[0]);
        final String permission2 = (argument2 == null) ? null : argument2.getPermission();
        if (argument2 == null || (permission2 != null && !sender.hasPermission(permission2))) {
            sender.sendMessage(ChatColor.RED + "Command not found");
            return true;
        }
        argument2.onCommand(sender, command, label, args);
        return true;
    }
}