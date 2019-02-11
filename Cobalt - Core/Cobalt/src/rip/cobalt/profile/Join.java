package rip.cobalt.profile;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.commands.staff.Authentication;
import rip.cobalt.database.MongoDB;
import rip.cobalt.util.Strings;

public class Join implements Listener {
	
	
	@EventHandler
	public void Join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("core.register")){
			Authentication.AuthenticationPhase.add(p.getUniqueId());
			p.sendMessage(ChatColor.RED + "Please authenticate!");
		}
		if(!e.getPlayer().hasPlayedBefore()) {
			MongoDB.getInstance().addPlayer(e.getPlayer());
			e.getPlayer().sendMessage(Strings.LOADINGPROFILE);
		}
		e.getPlayer().sendMessage(ChatColor.GREEN + "Your Profile has been loaded!");
		MongoDB.getInstance().readPlayer(e.getPlayer());
	}
}
