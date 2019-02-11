package net.wenjapvp.kohisg.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wenjapvp.kohisg.KohiSG;

public class HubCommand implements CommandExecutor
{

	/*    */ public static void teleport(Player pl, String input)
	/*    */ {
		/* 22 */ ByteArrayOutputStream b = new ByteArrayOutputStream();
		/* 23 */ DataOutputStream out = new DataOutputStream(b);
		/*    */ try
		{
			/* 25 */ out.writeUTF("Connect");
			/* 26 */ out.writeUTF(input);
			/*    */ }
		/*    */ catch (IOException localIOException)
		{}
		/* 29 */ pl.sendPluginMessage(KohiSG.getInstance(), "BungeeCord", b.toByteArray());
		/*    */ }

	/*    */
	/*    */ public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	/*    */ {
		/* 34 */ if (!(sender instanceof Player))
		{
			/* 35 */ sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			/* 36 */ return true;
			/*    */ }
		/* 38 */ Player p = (Player) sender;
		/* 39 */ teleport(p, "lobby");
		/* 40 */ p.sendMessage(ChatColor.RED + "You are being sent to the: " + ChatColor.YELLOW + "Hub!");
		/* 41 */ return false;
		/*    */ }
	/*    */ }
