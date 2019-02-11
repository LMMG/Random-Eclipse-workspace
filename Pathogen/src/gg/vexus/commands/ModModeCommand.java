package gg.vexus.commands;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import gg.vexus.handler.ModModeHandler;
import gg.vexus.handler.VanishHandler;

public class ModModeCommand implements CommandExecutor {

	public static ArrayList<String> modmode = new ArrayList<>();
	private static ArrayList<Player> players = new ArrayList();
	private static Random random = new Random();

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player player1 = (Player) arg0;
		if (arg1.getName().equalsIgnoreCase("mod")) {
			if ((!arg0.hasPermission("common.mod")) && (!arg0.hasPermission("common.mod"))) {
				arg0.sendMessage("");
				return true;
			}
			if (!(arg0 instanceof Player)) {
				arg0.sendMessage("");
				return true;
			}
			if (arg3.length == 0) {
				if (modmode.contains(arg0.getName())) {
					modmode.remove(arg0.getName());
					ModModeHandler.remove(Bukkit.getPlayer(arg0.getName()));
					arg0.sendMessage(
							"§eStaff mode of $p set to $b.".replace("$p", arg0.getName()).replace("$b", "false"));
					VanishHandler.unvanish(player1);
					return true;
				}
				modmode.add(arg0.getName());
				ModModeHandler.put(Bukkit.getPlayer(arg0.getName()));
				arg0.sendMessage("§eStaff mode of $p set to $b.".replace("$p", arg0.getName()).replace("$b", "true"));
				VanishHandler.vanish(player1);
				return true;
			}
			arg0.sendMessage("§cInvalid usage. This is a toggle command, no arguments.");
			return true;
		}
		if (arg1.getName().equalsIgnoreCase("modrtp")) {
			if ((!arg0.hasPermission("common.mod")) && (!arg0.hasPermission("common.mod"))) {
				arg0.sendMessage("§cNo Permission!");
				return true;
			}
			if (!(arg0 instanceof Player)) {
				arg0.sendMessage("§cPlayer only command.");
				return true;
			}
			if (arg3.length == 1) {
				Player[] arrayOfPlayer;
				int j;
				int i;
				if (arg3[0].equalsIgnoreCase("xray")) {
					players.clear();
					j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
					for (i = 0; i < j; i++) {
						Player p = arrayOfPlayer[i];
						if ((p.getLocation().getY() <= 20.0D) && (!p.hasPermission("core.modnogm"))
								&& (!p.hasPermission("core.mod")) && (!p.isOp()) && (!p.equals((Player) arg0))) {
							players.add(p);
						}
					}
					if (players.size() == 0) {
						arg0.sendMessage("§cThere aren't any players to teleport to.");
						return true;
					}
					int player = random.nextInt(players.size());
					((Player) arg0).teleport((Entity) players.get(player));
					arg0.sendMessage("§cRandomly teleported to §e" + ((Player) players.get(player)).getName() + "§c.");
					return true;
				}
				if (arg3[0].equalsIgnoreCase("any")) {
					players.clear();
					j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
					for (i = 0; i < j; i++) {
						Player p = arrayOfPlayer[i];
						if ((!p.hasPermission("common.mod")) && (!p.hasPermission("common.mod")) && (!p.isOp())
								&& (!p.equals((Player) arg0))) {
							players.add(p);
						}
					}
					if (players.size() == 0) {
						arg0.sendMessage("§cThere aren't any players to teleport to.");
						return true;
					}
					int player = random.nextInt(players.size());
					((Player) arg0).teleport((Entity) players.get(player));
					arg0.sendMessage("§cRandomly teleported to §e" + ((Player) players.get(player)).getName() + "§c.");
					return true;
				}
				arg0.sendMessage("Invalid arguments.");
				return true;
			}
			arg0.sendMessage("§cUsage: /modrtp <xray | any>");
			return true;
		}
		if (arg1.getName().equalsIgnoreCase("invcheck")) {
			if ((!arg0.hasPermission("common.mod")) && (!arg0.hasPermission("common.mod"))) {
				arg0.sendMessage("");
				return true;
			}
			if (!(arg0 instanceof Player)) {
				arg0.sendMessage("");
				return true;
			}
			if (arg3.length == 1) {
				return true;
			}
			arg0.sendMessage("§cUsage: /invcheck <player>");
			return true;
		}
		return true;
	}
}
