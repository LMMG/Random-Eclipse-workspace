package net.nersa.kitmap.command;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.com.google.common.primitives.Ints;
import net.nersa.kitmap.HCF;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.doctordark.internal.com.doctordark.base.BaseConstants;
import com.doctordark.util.BukkitUtils;
import com.doctordark.util.JavaUtils;
import com.google.common.collect.ImmutableList;

public class EconomyCommand
  implements CommandExecutor, TabCompleter
{
  private final HCF plugin;
  private static final ImmutableList<String> TAKE = ImmutableList.of("take", "negate", "minus", "subtract");
  private static final ImmutableList<String> GIVE;
  
  static
  {
    GIVE = ImmutableList.of("give", "add");
  }
  
  private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("add", "set", "take");
  
  public EconomyCommand(HCF plugin)
  {
    this.plugin = plugin;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    boolean hasStaffPermission = sender.hasPermission(command.getPermission() + ".staff");
    OfflinePlayer target;
    if ((args.length > 0) && (hasStaffPermission))
    {
      if ((args[0].equalsIgnoreCase("all")) || ((args[0].equalsIgnoreCase("*")) && (hasStaffPermission)))
      {
        Integer amount = Ints.tryParse(args[1]);
        for (UUID user : plugin.getPlayerManager().getAllData().keySet()) {
          this.plugin.getEconomyManager().addBalance(user, amount.intValue());
        }
        Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + " gave all players " + amount);
        return true;
      }
      target = BukkitUtils.offlinePlayerWithNameOrUUID(args[0]);
    }
    else
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
        return true;
      }
      target = (OfflinePlayer)sender;
    }
    if ((!target.hasPlayedBefore()) && (!target.isOnline()))
    {
      sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, new Object[] { args[0] }));
      return true;
    }
    UUID uuid = target.getUniqueId();
    int balance = this.plugin.getEconomyManager().getBalance(uuid);
    if ((args.length < 2) || (!hasStaffPermission))
    {
      sender.sendMessage(ChatColor.GOLD + (sender.equals(target) ? "Your balance" : new StringBuilder("Balance of ").append(target.getName()).toString()) + " is " + ChatColor.WHITE + '$' + balance + ChatColor.GOLD + '.');
      return true;
    }
    if (GIVE.contains(args[1].toLowerCase()))
    {
      if (args.length < 3)
      {
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
        return true;
      }
      Integer amount = Ints.tryParse(args[2]);
      if (amount == null)
      {
        sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
        return true;
      }
      int newBalance = this.plugin.getEconomyManager().addBalance(uuid, amount.intValue());
      sender.sendMessage(new String[] { ChatColor.GOLD + "Added " + '$' + JavaUtils.format(amount) + " to balance of " + target.getName() + '.', ChatColor.GOLD + "Balance of " + target.getName() + " is now " + '$' + newBalance + '.' });
      return true;
    }
    if (TAKE.contains(args[1].toLowerCase()))
    {
      if (args.length < 3)
      {
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
        return true;
      }
      Integer amount = Ints.tryParse(args[2]);
      if (amount == null)
      {
        sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
        return true;
      }
      int newBalance = this.plugin.getEconomyManager().subtractBalance(uuid, amount.intValue());
      sender.sendMessage(new String[] { ChatColor.GOLD + "Taken " + '$' + JavaUtils.format(amount) + " from balance of " + target.getName() + '.', ChatColor.GOLD + "Balance of " + target.getName() + " is now " + '$' + newBalance + '.' });
      return true;
    }
    if (!args[1].equalsIgnoreCase("set"))
    {
      sender.sendMessage(ChatColor.GOLD + (sender.equals(target) ? "Your balance" : new StringBuilder("Balance of ").append(target.getName()).toString()) + " is " + ChatColor.WHITE + '$' + balance + ChatColor.GOLD + '.');
      return true;
    }
    if (args.length < 3)
    {
      sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + target.getName() + ' ' + args[1] + " <amount>");
      return true;
    }
    Integer amount = Ints.tryParse(args[2]);
    if (amount == null)
    {
      sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
      return true;
    }
    int newBalance = this.plugin.getEconomyManager().setBalance(uuid, amount.intValue());
    sender.sendMessage(ChatColor.GOLD + "Set balance of " + target.getName() + " to " + '$' + JavaUtils.format(Integer.valueOf(newBalance)) + '.');
    return true;
  }
  
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
  {
    return args.length == 1 ? null : args.length == 2 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();
  }
}
