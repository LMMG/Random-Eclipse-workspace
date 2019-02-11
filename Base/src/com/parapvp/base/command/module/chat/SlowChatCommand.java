package com.parapvp.base.command.module.chat;

import com.parapvp.base.BasePlugin;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.manager.ServerHandler;
import com.parapvp.util.JavaUtils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SlowChatCommand
  extends BaseCommand
{
  private static final long DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(5L);
  private final BasePlugin plugin;
  
  public SlowChatCommand(BasePlugin plugin)
  {
    super("slowchat", "Slows the chat down for non-staff.");
    setAliases(new String[] { "slow" });
    setUsage("/(command)");
    this.plugin = plugin;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    long oldTicks = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
    Long newTicks;
    if (oldTicks > 0L)
    {
      newTicks = Long.valueOf(0L);
    }
    else
    {
      if (args.length < 1)
      {
        newTicks = Long.valueOf(DEFAULT_DELAY);
      }
      else
      {
        newTicks = Long.valueOf(JavaUtils.parse(args[0]));
        if (newTicks.longValue() == -1L)
        {
          sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
          return true;
        }
      }
    }
    this.plugin.getServerHandler().setChatSlowedMillis(newTicks.longValue());
    Bukkit.broadcastMessage(ChatColor.YELLOW + "Global chat is " + (newTicks.longValue() > 0L ? "has slowed down for  " + ChatColor.RED + DurationFormatUtils.formatDurationWords(newTicks.longValue(), true, true) : new StringBuilder().append(ChatColor.YELLOW).append("no longer slowed").toString()) + ChatColor.YELLOW + '.');
    return true;
  }
}
