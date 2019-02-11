package gg.vexus.commands.essentials.commands.pm;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import gg.vexus.Core;


public class MessageCommand
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    Player player = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("msg"))
    {
      if (args.length < 2)
      {
        sender.sendMessage(ChatColor.RED + "Usage: /msg <player> <message>");
        return false;
      }
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null)
      {
        sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
        return false;
      }
      if (target == player)
      {
        sender.sendMessage(ChatColor.RED + "You cannot message yourself!");
        return false;
      }
      StringBuilder msg = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
        msg.append(ChatColor.GREEN + args[i]).append(" ");
      }
      target.sendMessage(ChatColor.GREEN + "(From " + player.getDisplayName() + ChatColor.GREEN + ") " + ChatColor.RESET + msg.toString());
      player.sendMessage(ChatColor.GREEN + "(To " + target.getDisplayName() + ChatColor.GREEN + ") " + ChatColor.RESET + msg.toString());
      target.playSound(target.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
      target.setMetadata("reply", new FixedMetadataValue(Core.getCore(), player));
      player.setMetadata("reply", new FixedMetadataValue(Core.getCore(), target));
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("reply"))
    {
      if (args.length < 1)
      {
        sender.sendMessage(ChatColor.RED + "Usage: /r <message>");
        return false;
      }
      if (!player.hasMetadata("reply"))
      {
        sender.sendMessage(ChatColor.RED + "You don't have any messages to reply!");
        return false;
      }
      Player target = (Player)((MetadataValue)player.getMetadata("reply").get(0)).value();
      if (target == null)
      {
        sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
        return false;
      }
      if (target == player)
      {
        sender.sendMessage(ChatColor.RED + "You cannot message yourself!");
        return false;
      }
      StringBuilder msg = new StringBuilder();
      for (int i = 0; i < args.length; i++) {
        msg.append(ChatColor.GREEN + args[i]).append(" ");
      }
      target.sendMessage(ChatColor.GREEN + "(From " + player.getDisplayName() + ChatColor.GREEN + ") " + msg.toString());
      sender.sendMessage(ChatColor.GREEN + "(To " + target.getDisplayName() + ChatColor.GREEN + ") " + msg.toString());
      target.playSound(target.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);
      target.setMetadata("reply", new FixedMetadataValue(Core.getCore(), player));
      player.setMetadata("reply", new FixedMetadataValue(Core.getCore(), target));
    }
    return false;
  }
}

