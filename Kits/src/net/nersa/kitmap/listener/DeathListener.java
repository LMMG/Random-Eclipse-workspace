package net.nersa.kitmap.listener;

import net.minecraft.server.v1_7_R4.EntityLightning;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;
import net.nersa.player.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.TimeUnit;

public class DeathListener implements Listener {

	private static long BASE_REGEN_DELAY = TimeUnit.MINUTES.toMillis(40L);
	private HCF plugin;

	public DeathListener(HCF plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onDeath(PlayerDeathEvent event) {
		Player dead = event.getEntity();
		PlayerData deadData = plugin.getPlayerManager().getPlayerData(dead);

		deadData.setDeaths(deadData.getDeaths() + 1);
		deadData.save();

		Player killer = event.getEntity().getKiller();

		if (killer != null) {
			PlayerData killerData = plugin.getPlayerManager().getPlayerData(killer);
			killerData.setKills(killerData.getKills() + 1);
			killerData.save();
		}

		HCF.getInstance().getStorageBackend().insertKill(killer, dead);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

		if (playerFaction != null) {
			Faction factionAt = plugin.getFactionManager().getFactionAt(player.getLocation());
			double dtrLoss = (1.0D * factionAt.getDtrLossMultiplier());

			playerFaction.setRemainingRegenerationTime(BASE_REGEN_DELAY + (playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L)));
			playerFaction.broadcast(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has been killed.");
		}

		/*Integer balance = 0;
		if (plugin.getEconomyManager().getBalance(player.getUniqueId()) > 0) {
			balance = plugin.getEconomyManager().getBalance(player.getUniqueId()) % 10;
			plugin.getEconomyManager().addBalance(player.getKiller().getUniqueId(), balance);
			plugin.getEconomyManager().subtractBalance(player.getUniqueId(), balance);
		}*/

		if (player.getKiller() != null && player.getKiller() != null) {
			player.getKiller().sendMessage(ChatColor.YELLOW + "You earned " + ChatColor.GOLD + "$250" + ChatColor.YELLOW + " for killing " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + ".");
			plugin.getEconomyManager().addBalance(player.getKiller().getUniqueId(), 250);
		}

		if (Bukkit.spigot().getTPS()[0] > 15) {
			Location location = player.getLocation();
			WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

			EntityLightning entityLightning = new EntityLightning(worldServer, location.getX(), location.getY(), location.getZ(), false);
			PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(entityLightning);

			for (Player target : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
				target.playSound(target.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
			}
		}
	}

}