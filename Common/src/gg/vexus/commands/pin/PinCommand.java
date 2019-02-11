package gg.vexus.commands.pin;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import gg.vexus.Core;
import gg.vexus.utils.Color;

	
public class PinCommand
  implements Listener, CommandExecutor
{
  private File pinFile;
  
  public PinCommand()
  {
    this.pinFile = new File(Core.getCore().getDataFolder(), "pin.yml");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    Player p = (Player)sender;
    FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getCore().getDataFolder(), "pin.yml"));
    if ((cmd.getName().equalsIgnoreCase("setpin")) && (sender.hasPermission("command.pin"))) {
      if (args.length != 1) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage&7: /setpin [pin]"));
      } else if ((args.length == 1) && (!data.contains(String.valueOf(p.getUniqueId())))) {
        try
        {
          int i = 0;
          i = Integer.parseInt(args[0]);
          if ((i >= 0) && (i <= 10000))
          {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPin successfully set."));
            data.set(String.valueOf(p.getUniqueId()), Integer.valueOf(args[0]));
            data.save(this.pinFile);
          }
          else
          {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNumber must be between 0-10000"));
          }
        }
        catch (NumberFormatException|IOException ex2)
        {
          Exception ex = null;
          Exception nfe = ex;
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a number."));
        }
      } else if ((args.length == 1) && (data.contains(String.valueOf(p.getUniqueId())))) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIf you wish to change your pin, please speak to the owner"));
      }
    }
    return false;
  }
  
  @EventHandler
  public void onNoPinMove(PlayerMoveEvent event)
  {
    Player p = event.getPlayer();
    FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getCore().getDataFolder(), "pin.yml"));
    if ((!data.contains(String.valueOf(p.getUniqueId()))) && (p.hasPermission("command.pin")))
    {
      event.setTo(event.getFrom());
      p.sendMessage(Color.translate("&eYou must set a &6&lPIN&e. &7&o(/setpin <pin>)"));
    }
  }
  
  @EventHandler
  public void onCommandBeforeLoggedIn(PlayerCommandPreprocessEvent e)
  {
    Player p = e.getPlayer();
    FileConfiguration data = YamlConfiguration.loadConfiguration(new File(Core.getCore().getDataFolder(), "pin.yml"));
    if ((!e.getMessage().startsWith("/setpin")) && (!data.contains(String.valueOf(p.getUniqueId()))) && (p.hasPermission("Core.command.pin")))
    {
      e.setCancelled(true);
      p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou must set a &6&lPIN&e. &7&o(/setpin <pin>)"));
    }
  }
}
