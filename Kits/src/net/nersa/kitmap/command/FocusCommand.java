package net.nersa.kitmap.command;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.FactionMember;
import net.nersa.kitmap.faction.event.FactionFocusChangeEvent;
import net.nersa.kitmap.faction.struct.Role;
import net.nersa.kitmap.faction.type.PlayerFaction;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.doctordark.util.BukkitUtils;

public class FocusCommand
  implements CommandExecutor
{
  public String getUsage(String label)
  {
    return org.bukkit.ChatColor.RED + "Usage: /" + label + " <playerName|remove>";
  }
  
  @SuppressWarnings({ "deprecation", "unused" })
public boolean onCommand(CommandSender cs, Command command, String label, String[] args)
  {
    Player player = (Player)cs;
    if (args.length != 1)
    {
      cs.sendMessage(getUsage(label));
      return true;
    }
    PlayerFaction playerFaction = HCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId());
    FactionMember targetMember = playerFaction.getMember(args[0]);
    if (playerFaction == null)
    {
      player.sendMessage(org.bukkit.ChatColor.RED + "You must be in a faction!");
      return true;
    }
    if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER)
    {
      player.sendMessage(org.bukkit.ChatColor.RED + "You must be a faction officer to kick members.");
      return true;
    }
    if (targetMember != null) {
    	player.sendMessage(ChatColor.RED + "You may not focus a member of your own faction!");
    	return true;
    }
    if ((Bukkit.getPlayer(args[0]) == null) && (!args[0].equalsIgnoreCase("remove")))
    {
      player.sendMessage(org.bukkit.ChatColor.RED + getUsage(label));
      return true;
    }
    if (!(playerFaction.getFocus() == null) && args[0].equalsIgnoreCase(Bukkit.getServer().getOfflinePlayer(playerFaction.getFocus()).getName()))
    {
        player.sendMessage(org.bukkit.ChatColor.RED + "You already have that player focused!");
      return true;
    }
    if ((playerFaction.getFocus() == null) && (args[0].equalsIgnoreCase("remove")))
    {
        player.sendMessage(org.bukkit.ChatColor.RED + "You dont have a focused player to remove!");
      return true;
    }
    if (args[0].equalsIgnoreCase("remove"))
    {
      playerFaction.broadcast(org.bukkit.ChatColor.LIGHT_PURPLE + cs.getName() + org.bukkit.ChatColor.YELLOW + " has removed the current focus!");
      Bukkit.getPluginManager().callEvent(new FactionFocusChangeEvent(playerFaction, null, playerFaction.getFocus()));
      playerFaction.setFocus(null);     
      return true;
    }
    Bukkit.getPluginManager().callEvent(new FactionFocusChangeEvent(playerFaction, Bukkit.getPlayer(args[0]), playerFaction.getFocus()));
    if (playerFaction.getFocus() == null) {
      playerFaction.broadcast(net.md_5.bungee.api.ChatColor.GREEN + cs.getName() + net.md_5.bungee.api.ChatColor.YELLOW + " has focused " + net.md_5.bungee.api.ChatColor.DARK_PURPLE + args[0]);
    } else {
      playerFaction.broadcast(net.md_5.bungee.api.ChatColor.GREEN + cs.getName() + net.md_5.bungee.api.ChatColor.YELLOW + " has removed the focus on " + net.md_5.bungee.api.ChatColor.DARK_PURPLE + BukkitUtils.offlinePlayerWithNameOrUUID(playerFaction.getFocus().toString()).getName() + net.md_5.bungee.api.ChatColor.YELLOW + " and has focused " + net.md_5.bungee.api.ChatColor.DARK_PURPLE + args[0]);
    }
    playerFaction.setFocus(Bukkit.getPlayer(args[0]).getUniqueId());
    return false;
  }
}
