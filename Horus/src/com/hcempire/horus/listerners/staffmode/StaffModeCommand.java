package com.hcempire.horus.listerners.staffmode;

import java.util.Collections;
import java.util.List;

import com.hcempire.horus.Horus;
import com.hcempire.horus.util.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


public class StaffModeCommand implements CommandExecutor, TabCompleter
{
  private final Horus utilities = Horus.getInstance();
    
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
  {
    if (sender instanceof Player)
    {
      Player player = (Player) sender;
      if (player.hasPermission("andromeda.command.staffmode"))
      {
        if (arguments.length > 0)
        { 
          player.sendMessage(Color.translate("&cUsage: /" + label));
        }
        else
        {
          if (utilities.getStaffModeListener().isStaffModeActive(player))
          {
            utilities.getStaffModeListener().setStaffMode(player, false);
            player.sendMessage(Color.translate(Horus.getInstance().getLangFile().getString("STAFFMODE.DISABLED")));
          }
          else
          {
            utilities.getStaffModeListener().setStaffMode(player, true);
            player.sendMessage(Color.translate(Horus.getInstance().getLangFile().getString("STAFFMODE.ENABLED")));
          }
        }
      }
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