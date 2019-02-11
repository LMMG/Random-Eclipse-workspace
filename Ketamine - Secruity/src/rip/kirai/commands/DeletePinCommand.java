package rip.kirai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rip.kirai.database.MongoDB;

public class DeletePinCommand implements CommandExecutor
{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args)
	{
		if (!(commandSender instanceof Player)) { return true; }
		Player p = (Player) commandSender;
		if (cmd.getName().equalsIgnoreCase("deletepin"))
		{
			if (commandSender.hasPermission("core.deletepin"))
			{
				if (args.length == 0)
				{
					p.sendMessage(ChatColor.YELLOW + " /deletepin <Player>");
					return true;
				}
				if (args.length == 1)
				{
					Player target = Bukkit.getPlayer(args[0]);
					if (target == null)
					{
						target = (Player) Bukkit.getOfflinePlayer(args[0]);
						return true;
					}
					if (!MongoDB.getInstance().isStaff(target))
					{
						p.sendMessage(ChatColor.RED + "The player " + ChatColor.YELLOW + target + " isn't staff!");
						return true;
					}
					MongoDB.getInstance().RemoveStaffPin(target);
					if (target.getName().equalsIgnoreCase(commandSender.getName()))
					{
						p.sendMessage(ChatColor.GREEN + "You removed your pin!");
						return true;
					}
					p.sendMessage(ChatColor.GREEN + "You removed " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s pin!");
				}
			}
		}
		return true;
	}
}
