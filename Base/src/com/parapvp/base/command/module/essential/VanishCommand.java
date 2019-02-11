package com.parapvp.base.command.module.essential;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.Color;

public class VanishCommand extends BaseCommand
{
  private final BasePlugin utilities = BasePlugin.getPlugin();
    
  public VanishCommand() {
      super("vanish", "Toggles flight mode for a player.");
      this.setUsage("/(command) <playerName>");
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage(new Color().translateFromString("&cYou can not execute this command on console."));
      return false;
    }
    
    Player player = (Player) sender;
    if (player.hasPermission("core.command.vanish"))
    {
      if (arguments.length > 0)
      { 
        player.sendMessage(new Color().translateFromString("&cUsage: /" + label));
        return true;
      }
      
      if (arguments.length == 0)
      {
        if (utilities.getStaffModeListener().isVanished(player))
        {
          utilities.getStaffModeListener().setVanished(player, false);
          player.sendMessage(new Color().translateFromString("&eYou have &cdisabled &eyour vanish mode."));
        }
        else
        {
          utilities.getStaffModeListener().setVanished(player, true);
          player.sendMessage(new Color().translateFromString("&eYou have &aenabled &eyour vanish mode."));
        }
      }
    }
    else
    {
      player.sendMessage(new Color().translateFromString("&cYou do not have permissions to execute this command."));
    }
    
    return true;
  }
  
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (arguments.length > 1)
    {
      return Collections.emptyList();
    }
    return null;
  }
}