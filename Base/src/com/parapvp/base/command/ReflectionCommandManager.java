package com.parapvp.base.command;

import com.parapvp.base.BasePlugin;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class ReflectionCommandManager
  implements CommandManager
{
  private static final String PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission for this command.";
  private final Map<String, BaseCommand> commandMap;
  
  public ReflectionCommandManager(final BasePlugin plugin)
  {
    this.commandMap = new HashMap();
    final ConsoleCommandSender console = Bukkit.getConsoleSender();
    final Server server = Bukkit.getServer();
    server.getScheduler().runTaskLater(plugin, new Runnable()
    {
      public void run()
      {
        Optional<CommandMap> optionalCommandMap = ReflectionCommandManager.this.getCommandMap(server);
        if (!optionalCommandMap.isPresent())
        {
          Bukkit.broadcastMessage('[' + plugin.getDescription().getFullName() + "] Command map not found");
          
          console.sendMessage('[' + plugin.getDescription().getFullName() + "] Command map not found");
          return;
        }
        CommandMap bukkitCommandMap = (CommandMap)optionalCommandMap.get();
        for (BaseCommand command : ReflectionCommandManager.this.commandMap.values())
        {
          String commandName = command.getName();
          Optional<PluginCommand> optional = ReflectionCommandManager.this.getPluginCommand(commandName, plugin);
          if (optional.isPresent())
          {
            PluginCommand pluginCommand = (PluginCommand)optional.get();
            pluginCommand.setAliases(Arrays.asList(command.getAliases()));
            pluginCommand.setDescription(command.getDescription());
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
            pluginCommand.setUsage(command.getUsage());
            pluginCommand.setPermission(command.getPermission());
            pluginCommand.setPermissionMessage(ReflectionCommandManager.PERMISSION_MESSAGE);
            bukkitCommandMap.register(plugin.getDescription().getName(), pluginCommand);
          }
          else
          {
            Bukkit.broadcastMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
            console.sendMessage('[' + plugin.getName() + "] " + ChatColor.YELLOW + "Failed to register command '" + commandName + "'.");
          }
        }
      }
    }, 1L);
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
  
  private Optional<PluginCommand> getPluginCommand(String name, Plugin plugin)
  {
    try
    {
      Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(new Class[] { String.class, Plugin.class });
      constructor.setAccessible(true);
      return Optional.of(constructor.newInstance(new Object[] { name, plugin }));
    }
    catch (IllegalArgumentException|IllegalAccessException|InstantiationException|InvocationTargetException|NoSuchMethodException|SecurityException ex)
    {
      ex.printStackTrace();
    }
    return Optional.empty();
  }
  
  private Optional<CommandMap> getCommandMap(Server server)
  {
    PluginManager pluginManager = server.getPluginManager();
    if ((pluginManager instanceof SimplePluginManager)) {
      try
      {
        Field field = SimplePluginManager.class.getDeclaredField("commandMap");
        field.setAccessible(true);
        return Optional.of((CommandMap)field.get(pluginManager));
      }
      catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException ex)
      {
        ex.printStackTrace();
        return Optional.empty();
      }
    }
    return Optional.empty();
  }
}
