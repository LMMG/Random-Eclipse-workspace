package gg.vexus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;

public class CommandBlocker implements Listener
{

	public static String cmd;
	public static String Title;
	public static String SubTitle;
	public static String ActionBar;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onpl(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		this.cmd = e.getMessage().split(" ", 2)[0];
		if ((!p.hasPermission("pb.bypass")) && ((this.cmd.equals("/pl")) || (this.cmd.equals("/plugin")) || (this.cmd.equals("/plugins")) || (this.cmd.contains(":")) || (this.cmd.contains("/ ")) || (this.cmd.contains("/bukkit:")) || (this.cmd.contains("/?")) || (this.cmd.contains("/icanhasbukkit")) || (this.cmd.contains("ver")) || (this.cmd.contains("/version")) || (this.cmd.contains("/about"))))
		{
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "Oh no no no a young man!");
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		Bukkit.getLogger().severe("msg: " + event.getMessage());
		if ((event.getMessage().startsWith("/perms")) && (!event.getPlayer().hasPermission("wenja.perms")))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand12(PlayerCommandPreprocessEvent event)
	{
		Bukkit.getLogger().severe("msg: " + event.getMessage());
		if ((event.getMessage().startsWith("/perm")) && (!event.getPlayer().hasPermission("wenja.perms")))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		}
	}

	@EventHandler
	public void onCommandProcess(PlayerCommandPreprocessEvent e)
	{
		final String[] withArguments = e.getMessage().split(" ");
		final String command = withArguments[0].substring(1);
		if (command.contains(":"))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot use any commands with &b:&c in it!"));
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCommand1(PlayerCommandPreprocessEvent event)
	{
		Bukkit.getLogger().severe("msg: " + event.getMessage());
		if ((event.getMessage().startsWith("/perm")) && (!event.getPlayer().hasPermission("wenja.perms")))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage("Unknown command. Type \"/help\" for help.");
		}
	}
}