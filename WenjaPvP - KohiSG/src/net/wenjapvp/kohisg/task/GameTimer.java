package net.wenjapvp.kohisg.task;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.wenjapvp.kohisg.GameState;
import net.wenjapvp.kohisg.KohiSG;
import net.wenjapvp.kohisg.commands.HubCommand;
import net.wenjapvp.kohisg.loot.ChestItemStack;
import net.wenjapvp.kohisg.utils.Color;
import net.wenjapvp.kohisg.utils.ItemStackUtils;
import net.wenjapvp.kohisg.utils.PlayerUtils;

public class GameTimer extends BukkitRunnable
{
	private final KohiSG kohiSG = KohiSG.getInstance();

	private int left;

	private boolean shutdown;
	private boolean forceStart;

	public void start()
	{
		runTaskTimer(kohiSG, 20L, 20L);
	}

	public int getLeft()
	{
		return left;
	}

	public void setLeft(int left)
	{
		this.left = left;
	}

	public void setForceStart(boolean forceStart)
	{
		this.forceStart = forceStart;
	}

	public void spawnFeast()
	{
		for (int x = 0; x < 16; x++)
		{
			Chest chest;
			for (int z = 0; z < 16; z++)
			{
				if (RandomUtils.nextInt(20) == 0)
				{
					Block block = ((World) Bukkit.getWorlds().get(0)).getBlockAt(x, 65, z);
					block.setType(Material.CHEST);
					chest = (Chest) block.getState();
					for (ChestItemStack item : kohiSG.getFeastConfiguration().getItemList())
					{
						if (item.hasChance(RandomUtils.JVM_RANDOM))
						{
							ItemStack items = item.getRandomItemStack(RandomUtils.JVM_RANDOM);
							chest.getInventory().addItem(new ItemStack[] {
									items
							});
						}
					}
				}
			}
		}
		Block block = ((World) Bukkit.getWorlds().get(0)).getBlockAt(8, 65, 8);
		block.setType(Material.ENCHANTMENT_TABLE);
	}

	private void broadcastFeast()
	{
		int left = this.left - 180;
		int minute;
		if (left > 0 && left % 60 == 0)
		{
			minute = left / 60;
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
				player.sendMessage(Color.translate("&eThe &aFeast &ewill spawn at 0, 0 in &6" + minute + " &eminutes."));
			}
		}
		else if (left <= 60)
		{
			if (left == 30)
			{
				for (Player player : Bukkit.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
					player.sendMessage(Color.translate("&eThe &aFeast &ewill spawn at 0, 0 in &6" + left + " &eseconds."));
				}
			}
			if (left < 15 && left > 0)
			{
				for (Player player : Bukkit.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
					player.sendMessage(Color.translate("&eThe &aFeast &ewill spawn at 0, 0 in &6" + left + " &eseconds."));
				}
			}
		}
	}

	private void broadcastStart()
	{
		int minute;
		if (left > 0 && left % 60 == 0)
		{
			minute = left / 60;
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
				player.sendMessage(Color.translate("&aGame starting in &e" + minute + " &aminutes."));
			}
		}
		else if (left <= 60)
		{
			if (left == 30)
			{
				for (Player player : Bukkit.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
					player.sendMessage(Color.translate("&aGame starting in &e" + left + " &aseconds."));
				}
			}
			if (left < 15 && left > 0)
			{
				for (Player player : Bukkit.getServer().getOnlinePlayers())
				{
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
					player.sendMessage(Color.translate("&aGame starting in &e" + left + " &aseconds."));
				}
			}
		}
	}

	@Override
	public void run()
	{
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			Location location = player.getLocation();
			int absX = Math.abs(location.getBlockX());
			int absZ = Math.abs(location.getBlockZ());
			if (absX > kohiSG.getBorderManager().getBorder() || absZ > kohiSG.getBorderManager().getBorder())
			{
				player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
				player.sendMessage(Color.translate("&cWalking out of world border hurts!"));
			}
			else
			{
				if (absX <= kohiSG.getBorderManager().getBorder() - 10 && absZ <= kohiSG.getBorderManager().getBorder() - 10)
				{
					continue;
				}
				player.sendMessage(Color.translate("&cYou are near the world border. going further will hurt!"));
			}
		}
		--left;
		broadcastStart();
		if (kohiSG.getGameState() == GameState.WAITING)
		{
			if (Bukkit.getOnlinePlayers().length < 5 && !forceStart)
			{
				left = 60;
			}
			if (left <= 0)
			{
				left = 601;
				kohiSG.setGameState(GameState.GAME);
			}
		}
		else if (kohiSG.getGameState() == GameState.GAME)
		{
			if (left == 600)
			{
				Location location2 = new Location((World) Bukkit.getWorlds().get(0), 8.0, 66.0, 8.0);
				for (Player players : Bukkit.getServer().getOnlinePlayers())
				{
					kohiSG.getSpectateManager().unSpectate(players);
					PlayerUtils.clearInventory(players);
					players.teleport(location2);
					players.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
					players.setHealth(20.0);
					players.setSaturation(10.0f);
					players.setFoodLevel(20);
					PlayerInventory inventory = players.getInventory();
					inventory.setItem(8, ItemStackUtils.setItemTitle(new ItemStack(Material.COMPASS), "Player Finder"));
					players.sendMessage(Color.translate("&eYou now have &a60 &eseconds of &c&lPvP Prot&e."));
				}
				kohiSG.buildPlayerList();
			}

			if (left >= 180)
			{
				broadcastFeast();
			}
			if (left == 180)
			{
				spawnFeast();
				Bukkit.getServer().broadcastMessage(Color.translate("&eThe &aFeast &ehas been spawned!"));
			}

			if (left <= 0 || kohiSG.getPlayerList().size() <= 1)
			{
				left = 3600;
				kohiSG.setGameState(GameState.DEATH_MATH);
				Bukkit.getServer().broadcastMessage(Color.translate("&eThe World Border has started to &cshrink&e."));
			}
		}
		else if (kohiSG.getGameState() == GameState.DEATH_MATH)
		{
			kohiSG.getBorderManager().setBorder(kohiSG.getBorderManager().getBorder() - 1);
			kohiSG.getBorderManager().setBorder(Math.max(kohiSG.getBorderManager().getBorder(), 50));
			if (left <= 0 || kohiSG.getPlayerList().size() <= 1)
			{
				left = 1;
				kohiSG.setGameState(GameState.END);
			}
		}
		else if (kohiSG.getGameState() == GameState.END)
		{
			left = -1;
			if (!shutdown)
			{
				shutdown = true;
				new BukkitRunnable()
				{
					int shutdownTimer = 15;

					@Override
					public void run()
					{
						--shutdownTimer;
						if (shutdownTimer > 1)
						{
							Bukkit.getServer().broadcastMessage(Color.translate("&cServer will shuting down in " + shutdownTimer + " seconds."));
						}
						else
						{
							Bukkit.getServer().broadcastMessage(Color.translate("&cServer will shuting down in " + shutdownTimer + " second."));
						}
						if (shutdownTimer <= 0)
						{
							Bukkit.getServer().shutdown();
						}
					}
				}.runTaskTimer(kohiSG, 20L, 20L);
			}
		}
	}
}