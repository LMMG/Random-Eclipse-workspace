package com.parapvp.base.user;

import com.google.common.base.Preconditions;
import com.parapvp.base.BasePlugin;
import com.parapvp.util.Config;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UserManager
{
  private final ConsoleUser console;
  private final JavaPlugin plugin;
  private Config userConfig;
  private Map<UUID, ServerParticipator> participators;
  
  public UserManager(BasePlugin plugin)
  {
    this.plugin = plugin;
    reloadParticipatorData();
    ServerParticipator participator = (ServerParticipator)this.participators.get(ConsoleUser.CONSOLE_UUID);
    if (participator != null) {
      this.console = ((ConsoleUser)participator);
    } else {
      this.participators.put(ConsoleUser.CONSOLE_UUID, this.console = new ConsoleUser());
    }
  }
  
  public ConsoleUser getConsole()
  {
    return this.console;
  }
  
  public Map<UUID, ServerParticipator> getParticipators()
  {
    return this.participators;
  }
  
  public ServerParticipator getParticipator(CommandSender sender)
  {
    Preconditions.checkNotNull(sender, "CommandSender cannot be null");
    if ((sender instanceof ConsoleCommandSender)) {
      return this.console;
    }
    if ((sender instanceof Player)) {
      return (ServerParticipator)this.participators.get(((Player)sender).getUniqueId());
    }
    return null;
  }
  
  public ServerParticipator getParticipator(UUID uuid)
  {
    Preconditions.checkNotNull(uuid, "Unique ID cannot be null");
    return (ServerParticipator)this.participators.get(uuid);
  }
  
  public BaseUser getUser(UUID uuid)
  {
    ServerParticipator participator = getParticipator(uuid);
    BaseUser baseUser;
    if ((participator != null) && ((participator instanceof BaseUser))) {
      baseUser = (BaseUser)participator;
    } else {
      this.participators.put(uuid, baseUser = new BaseUser(uuid));
    }
    return baseUser;
  }
  
  public void reloadParticipatorData()
  {
    this.userConfig = new Config(this.plugin, "participators");
    Object object = this.userConfig.get("participators");
    if ((object instanceof MemorySection))
    {
      MemorySection section = (MemorySection)object;
      Set<String> keys = section.getKeys(false);
      this.participators = new HashMap(keys.size());
      for (String id : keys) {
        this.participators.put(UUID.fromString(id), (ServerParticipator)this.userConfig.get("participators." + id));
      }
    }
    else
    {
      this.participators = new HashMap();
    }
  }
  
  public void saveParticipatorData()
  {
    Map<String, ServerParticipator> saveMap = new LinkedHashMap(this.participators.size());
    for (Map.Entry<UUID, ServerParticipator> entry : this.participators.entrySet()) {
      saveMap.put(((UUID)entry.getKey()).toString(), entry.getValue());
    }
    this.userConfig.set("participators", saveMap);
    this.userConfig.save();
  }
}
