/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.parapvp.base.command.module.essential;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ListCommand implements CommandExecutor {
	
	/*	
	 * 
	 * Other /list type HCTeams
	 * 
	 */
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("list")){
			ArrayList<String> playernames = new ArrayList<String>();
			for(Player p : Bukkit.getOnlinePlayers()){
				playernames.add(getNameColour(p.getName()) + p.getName() + ChatColor.WHITE);
			}
			Player p  = (Player) sender;
			message(p);
			sender.sendMessage(playernames.toString());
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static ChatColor getNameColour(String name){
		if(Bukkit.getPlayer(name)==null){
			return ChatColor.WHITE;
		}
		Player p = (Player) Bukkit.getPlayer(name);
		PermissionUser u = PermissionsEx.getUser(p);
		PermissionGroup g = u.getGroups()[0];
		if(g.getName().equalsIgnoreCase("Owner")){
			return ChatColor.DARK_RED;
		}else if(g.getName().equalsIgnoreCase("Developer")){
			return ChatColor.YELLOW;
		}else if(g.getName().equalsIgnoreCase("Platform Admin")){
			return ChatColor.DARK_RED;
		}else if(g.getName().equalsIgnoreCase("SrAdmin")){
			return ChatColor.RED;
		}else if(g.getName().equalsIgnoreCase("Admin")){
			return ChatColor.RED;
		}else if(g.getName().equalsIgnoreCase("SrMod")){
			return ChatColor.DARK_AQUA;
		}else if(g.getName().equalsIgnoreCase("Moderator")){
			return ChatColor.DARK_AQUA;
		}else if(g.getName().equalsIgnoreCase("Trial Mod")){
			return ChatColor.DARK_AQUA;
		}else if(g.getName().equalsIgnoreCase("Alpha")){
			return ChatColor.GOLD;
		}else if(g.getName().equalsIgnoreCase("Myth")){
			return ChatColor.BLUE;
		}else if(g.getName().equalsIgnoreCase("Delta")){
			return ChatColor.LIGHT_PURPLE;
		} else if (g.getName().equalsIgnoreCase("Elite")) {
			return ChatColor.DARK_PURPLE;
		} else if(g.getName().equalsIgnoreCase("Mvp")) {
			return ChatColor.GREEN;
		} else if (g.getName().equalsIgnoreCase("Bravo")) {
			return ChatColor.DARK_PURPLE;
		}else if (g.getName().equalsIgnoreCase("default")){
			return ChatColor.YELLOW;
		}
		return null;
	}
	public void message(Player player) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Owner&f, &eDeveloper, &4Platform Admin&f, &cSrAdmin&f, "
                + "&cAdmin&f, &5SrMod&f, &3Mod&f, &bTrial Mod&f, &6Alpha&f, &9Myth&f, &aMvp&f, &dDelta&f, &5Bravo,  &eMember"));
	}
}