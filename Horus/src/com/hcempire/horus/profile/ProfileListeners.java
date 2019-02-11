package com.hcempire.horus.profile;

import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.utils.player.PlayerUtility;
import com.bizarrealex.aether.event.BoardCreateEvent;
import com.hcempire.horus.Horus;
import com.hcempire.horus.listerners.enchantments.EnchantmentListeners;
import com.hcempire.horus.profile.kit.ProfileKitListeners;
import com.hcempire.horus.profile.kit.ability.ProfileKitAbilityListeners;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ProfileListeners implements Listener
{

	public ProfileListeners(Horus main)
	{
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new ProfileKitAbilityListeners(), main);
		pluginManager.registerEvents(new ProfileKitListeners(), main);
		// pluginManager.registerEvents(new EnchantmentListeners(), main);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
	{
		if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED)
		{
			Profile profile = new Profile(event.getUniqueId());

			if (profile.getName() == null || !profile.getName().equals(event.getName()))
			{
				profile.setName(event.getName());
			}
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		Profile profile = Profile.getByPlayer(event.getPlayer());

		event.setQuitMessage(null);

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Profile.getProfiles().remove(profile);
				profile.setPlayTime(event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) / 20 * 1000);
			}
		}.runTaskAsynchronously(Horus.getInstance());
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		com.alexandeh.ekko.profiles.Profile.getByUuid(event.getPlayer().getUniqueId()).updateTab();
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
	{

		Claim claim = Claim.getProminentClaimAt(event.getEntity().getLocation());
		if (claim != null && claim.getFaction() instanceof SystemFaction)
		{
			if (!((SystemFaction) claim.getFaction()).isDeathban())
			{
				event.setFoodLevel(20);
				return;
			}
		}

		if (new Random().nextInt(3) != 3)
		{
			event.setCancelled(true);
		}

	}

}
