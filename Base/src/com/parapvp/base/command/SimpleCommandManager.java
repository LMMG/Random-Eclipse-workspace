package com.parapvp.base.command;

import com.parapvp.base.BasePlugin;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.scheduler.BukkitRunnable;

public class SimpleCommandManager
  implements CommandManager
{
  private static final String PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission to execute this command.";
  private final Map<String, BaseCommand> commandMap;
  
  public SimpleCommandManager(final BasePlugin plugin)
  {
    this.commandMap = new HashMap();
    final ConsoleCommandSender console = plugin.getServer().getConsoleSender();
    new BukkitRunnable()
    {
      public void run()
      {
        Collection<BaseCommand> commands = SimpleCommandManager.this.commandMap.values();
        for (BaseCommand command : commands)
        {
          String commandName = command.getName();
          PluginCommand pluginCommand = plugin.getCommand(commandName);
          if (pluginCommand == null)
          {
            Bukkit.broadcastMessage(commandName);
            console.sendMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
            console.sendMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Reason: Undefined in plugin.yml.");
          }
          else
          {
            pluginCommand.setAliases(Arrays.asList(command.getAliases()));
            pluginCommand.setDescription(command.getDescription());
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
            pluginCommand.setUsage(command.getUsage());
            pluginCommand.setPermission("base.command." + command.getName());
            pluginCommand.setPermissionMessage(SimpleCommandManager.PERMISSION_MESSAGE);
          }
        }
      }
    }
    
      .runTask(plugin);
  }
  
  public boolean containsCommand(BaseCommand command)
  {
    return this.commandMap.containsValue(command);
  }
  
  public void registerAll(BaseCommandModule module)
  {
    if (module.isEnabled())
    {
      Set<BaseCommand> commands = module.getCommands();
      for (BaseCommand command : commands) {
        this.commandMap.put(command.getName(), command);
      }
    }
  }
  
  public void registerCommand(BaseCommand command)
  {
    this.commandMap.put(command.getName(), command);
  }
  
  public void registerCommands(BaseCommand[] commands)
  {
    for (BaseCommand command : commands) {
      this.commandMap.put(command.getName(), command);
    }
  }
  
  public void unregisterCommand(BaseCommand command)
  {
    this.commandMap.values().remove(command);
  }
  
  public BaseCommand getCommand(String id)
  {
    return (BaseCommand)this.commandMap.get(id);
  }
}
