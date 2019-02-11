package gg.vexus.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.chat.ClickAction;
import com.doctordark.util.chat.Text;

import gg.vexus.utils.PlayerTimer;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class ReportCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("report"))
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage(ChatColor.RED + "You are not a player!");
        return true;
      }
      if ((args.length == 0) || (args.length == 1))
      {
        sender.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
        return true;
      }
      Player p = (Player)sender;
      if (PlayerTimer.isOnCooldown("report", p))
      {
        p.sendMessage(ChatColor.RED + "You are still on cooldown for another " + PlayerTimer.getCooldownForPlayerInt("report", p) + " seconds.");
        return true;
      }
      PlayerTimer.addCooldown("report", p, 45);
      String message = StringUtils.join(args, " ", 1, args.length);
      p.sendMessage(ChatColor.GREEN + "Thanks for your report.");
      Player[] arrayOfPlayer;
      if ((arrayOfPlayer = Bukkit.getOnlinePlayers()).length != 0)
      {
        Player staff = arrayOfPlayer[0];
        if (staff.hasPermission("common.report"))
        {
          Text text = new Text(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE.toString() + ChatColor.BOLD + "Report" + ChatColor.DARK_GRAY + "] " + 
            ChatColor.RED + p.getName() + 
            ChatColor.BLUE + " has reported " + ChatColor.RED + Bukkit.getPlayer(args[0]).getName())
            .setHoverText(ChatColor.GREEN + "Click to teleport to " + Bukkit.getPlayer(args[0]).getName() + ".").setClick(ClickAction.RUN_COMMAND, "/tp " + 
            Bukkit.getPlayer(args[0]).getName()).append(ChatColor.BLUE + " for '" + ChatColor.GRAY + message + ChatColor.BLUE + "'.");
          text.send(staff);
        }
        return true;
      }
    }
    return false;
  }
}
