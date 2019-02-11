package us.saifed.plugins.utilities.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.alexandeh.kraken.tab.PlayerTab;
import com.alexandeh.kraken.tab.TabEntry;
import com.alexandeh.kraken.tab.event.PlayerTabCreateEvent;

import server.wenjapvp.hcf.eventgame.EventTimer;
import server.wenjapvp.hcf.eventgame.faction.ConquestFaction;
import server.wenjapvp.hcf.eventgame.faction.EventFaction;
import server.wenjapvp.hcf.eventgame.faction.KothFaction;
import server.wenjapvp.hcf.faction.event.PlayerJoinFactionEvent;
import server.wenjapvp.hcf.faction.event.PlayerLeftFactionEvent;
import server.wenjapvp.hcf.faction.type.PlayerFaction;
import us.saifed.plugins.utilities.Utilities;

public class TabListener implements Listener
{
	private final Utilities utilities = Utilities.getInstance();

	private TabUpdateTask tabUpdateTask;

	public static String getCardinalDirection(Player player)
	{
		double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
		if (rotation < 0.0D)
		{
			rotation += 360.0D;
		}
		if ((0.0D <= rotation) && (rotation < 22.5D)) { return "N"; }
		if ((22.5D <= rotation) && (rotation < 67.5D)) { return "NE"; }
		if ((67.5D <= rotation) && (rotation < 112.5D)) { return "E"; }
		if ((112.5D <= rotation) && (rotation < 157.5D)) { return "SE"; }
		if ((157.5D <= rotation) && (rotation < 202.5D)) { return "S"; }
		if ((202.5D <= rotation) && (rotation < 247.5D)) { return "SW"; }
		if ((247.5D <= rotation) && (rotation < 292.5D)) { return "W"; }
		if ((292.5D <= rotation) && (rotation < 337.5D)) { return "NW"; }
		if ((337.5D <= rotation) && (rotation < 360.0D)) { return "N"; }
		return null;
	}

	@EventHandler
	public void onPlayerTabCreateEvent(PlayerTabCreateEvent event)
	{
		PlayerTab playerTab = event.getPlayerTab();
		tabUpdateTask = new TabUpdateTask(playerTab);
		tabUpdateTask.runTaskTimer(utilities, 0, 10L);
	}

	@EventHandler
	public void onPlayerJoinFaction(PlayerJoinFactionEvent event)
	{
		Player player = event.getPlayer().get();
		PlayerTab playerTab = PlayerTab.getByPlayer(player);
		if (playerTab != null)
		{
			clearAllSlots(playerTab);
		}
	}

	@EventHandler
	public void onPlayerLeftFaction(PlayerLeftFactionEvent event)
	{
		Player player = event.getPlayer().get();
		PlayerTab playerTab = PlayerTab.getByPlayer(player);
		if (playerTab != null)
		{
			clearAllSlots(playerTab);
		}
	}

	private void clearAllSlots(PlayerTab playerTab)
	{
		for (int i = 0; i < 60; i++)
		{
			int x = i % 3;
			int y = i / 3;
			playerTab.getByPosition(x, y).text(ChatColor.RESET.toString()).send();
		}
	}

	private class TabUpdateTask extends BukkitRunnable
	{
		private final Player player;
		private final PlayerTab playerTab;

		public TabUpdateTask(PlayerTab playerTab)
		{
			this.player = playerTab.getPlayer();
			this.playerTab = playerTab;
		}

		@Override
		public void run()
		{
			if (player.isOnline())
			{
				PlayerFaction playerFaction = utilities.getHCF().getFactionManager().getPlayerFaction(player);
				if (playerFaction != null)
				{
					playerTab.getByPosition(0, 0).text(new ColorUtils().translateFromString("&6&lHome:")).send();
					TabEntry homeCoords = playerTab.getByPosition(0, 1);
					if (playerFaction.getHome() == null)
					{
						homeCoords.text(new ColorUtils().translateFromString("&eNot Set")).send();
					}
					else
					{
						homeCoords.text(new ColorUtils().translateFromString("&e" + playerFaction.getHome().getBlockX() + ", " + playerFaction.getHome().getBlockY() + ", " + playerFaction.getHome().getBlockZ())).send();
					}

					playerTab.getByPosition(0, 3).text(new ColorUtils().translateFromString("&6&lFaction Info:")).send();
					double dtr = playerFaction.getDeathsUntilRaidable();
					double maxDtr = playerFaction.getMaximumDeathsUntilRaidable();
					playerTab.getByPosition(0, 4).text(new ColorUtils().translateFromString("&7DTR: " + dtr + "/" + maxDtr)).send();
					playerTab.getByPosition(0, 5).text(new ColorUtils().translateFromString("&7Online: " + playerFaction.getOnlinePlayers().size() + "/" + playerFaction.getMembers().size())).send();
					playerTab.getByPosition(0, 6).text(new ColorUtils().translateFromString("&7Balance: $" + playerFaction.getBalance())).send();

					playerTab.getByPosition(0, 8).text(new ColorUtils().translateFromString("&6&lPlayer Info:")).send();
					playerTab.getByPosition(0, 9).text(new ColorUtils().translateFromString("&7Kills: " + utilities.getHCF().getUserManager().getUser(player.getUniqueId()).getKills())).send();

					playerTab.getByPosition(0, 11).text(new ColorUtils().translateFromString("&6&lYour Location:")).send();
					playerTab.getByPosition(0, 12).text(new ColorUtils().translateFromString(utilities.getHCF().getFactionManager().getFactionAt(player.getLocation()).getDisplayName(player))).send();
					playerTab.getByPosition(0, 13).text(new ColorUtils().translateFromString("&7(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [" + getCardinalDirection(player) + "]")).send();

					TabEntry eventName = playerTab.getByPosition(0, 15);
					TabEntry eventTime = playerTab.getByPosition(0, 16);
					TabEntry eventCoords = playerTab.getByPosition(0, 17);
					EventTimer eventTimer = utilities.getHCF().getTimerManager().getEventTimer();
					EventFaction eventFaction = eventTimer.getEventFaction();
					if (eventFaction instanceof KothFaction)
					{
						KothFaction kothFaction = (KothFaction) eventFaction;
						int x = kothFaction.getCaptureZone().getCuboid().getMaximumPoint().getBlockX();
						int z = kothFaction.getCaptureZone().getCuboid().getMinimumPoint().getBlockZ();

						eventName.text(new ColorUtils().translateFromString("&4" + kothFaction.getName())).send();
						eventTime.text(new ColorUtils().translateFromString("&e" + TimeUtils.LongCountdown.setFormat(eventTimer.getRemaining()))).send();
						eventCoords.text(new ColorUtils().translateFromString("&e" + x + ", " + z)).send();
					}
					else if (eventFaction instanceof ConquestFaction)
					{
						ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
						eventName.text(new ColorUtils().translateFromString("&4 " + conquestFaction.getName())).send();
						eventTime.text(new ColorUtils().translateFromString("&c " + TimeUtils.LongCountdown.setFormat(conquestFaction.getRed().getRemainingCaptureMillis()) + "&b " + TimeUtils.LongCountdown.setFormat(conquestFaction.getBlue().getRemainingCaptureMillis()))).send();
						eventCoords.text(new ColorUtils().translateFromString("&e " + TimeUtils.LongCountdown.setFormat(conquestFaction.getYellow().getRemainingCaptureMillis()) + "&a " + TimeUtils.LongCountdown.setFormat(conquestFaction.getGreen().getRemainingCaptureMillis()))).send();
					}
					else
					{
						eventName.text(ChatColor.RESET.toString()).send();
						eventTime.text(ChatColor.RESET.toString()).send();
						eventCoords.text(ChatColor.RESET.toString()).send();
					}

					playerTab.getByPosition(1, 0).text(new ColorUtils().translateFromString("&6&lWenjaPvP &7[Kits]")).send();

					playerTab.getByPosition(1, 2).text(new ColorUtils().translateFromString("&7" + playerFaction.getName())).send();

					// List<Player> members = new ArrayList<>();
					// Player memberPlayer = Bukkit.getPlayer(members.get(0));
					//
					// for (int i = 3; i <=
					// playerFaction.getOnlinePlayers().size(); i++)
					// {
					// if (playerFaction.getMember(members.get(i)))
					// {
					//
					// }
					// }

					int index = 3;
					for (Player members : playerFaction.getOnlinePlayers())
					{
						switch (playerFaction.getMember(members).getRole())
						{
							case LEADER:
								playerTab.getByPosition(1, index++).text(ChatColor.GREEN + playerFaction.getMember(members).getRole().getAstrix() + members.getName()).send();
								continue;
							case CAPTAIN:
								playerTab.getByPosition(1, index++).text(ChatColor.GREEN + playerFaction.getMember(members).getRole().getAstrix() + members.getName()).send();
								continue;
							default:
								playerTab.getByPosition(1, index++).text(ChatColor.GREEN + playerFaction.getMember(members).getRole().getAstrix() + members.getName()).send();
								break;
						}
					}
					for (; index < 19; index++)
					{
						playerTab.getByPosition(1, index).text(ChatColor.RESET.toString()).send();
					}

					playerTab.getByPosition(2, 0).text(new ColorUtils().translateFromString("&6&lMap Kit:")).send();
					playerTab.getByPosition(2, 1).text(new ColorUtils().translateFromString("&7Sharpness: 1")).send();
					playerTab.getByPosition(2, 2).text(new ColorUtils().translateFromString("&7Protection: 1")).send();

					playerTab.getByPosition(2, 4).text(new ColorUtils().translateFromString("&6&lWorld Border:")).send();
					playerTab.getByPosition(2, 5).text(new ColorUtils().translateFromString("&72500, 2500")).send();

					playerTab.getByPosition(2, 7).text(new ColorUtils().translateFromString("&6&lEnd Portal:")).send();
					playerTab.getByPosition(2, 8).text(new ColorUtils().translateFromString("&71000, 1000")).send();
					playerTab.getByPosition(2, 9).text(new ColorUtils().translateFromString("&7In each quadrant.")).send();
				}
				else
				{
					playerTab.getByPosition(0, 0).text(new ColorUtils().translateFromString("&6&lPlayer Info:")).send();
					playerTab.getByPosition(0, 1).text(new ColorUtils().translateFromString("&7Kills: " + utilities.getHCF().getUserManager().getUser(player.getUniqueId()).getKills())).send();

					playerTab.getByPosition(0, 3).text(new ColorUtils().translateFromString("&6&lYour Location:")).send();
					playerTab.getByPosition(0, 4).text(new ColorUtils().translateFromString(utilities.getHCF().getFactionManager().getFactionAt(player.getLocation()).getDisplayName(player))).send();
					playerTab.getByPosition(0, 5).text(new ColorUtils().translateFromString("&7(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ") [" + getCardinalDirection(player) + "]")).send();

					TabEntry eventName = playerTab.getByPosition(0, 7);
					TabEntry eventTime = playerTab.getByPosition(0, 8);
					TabEntry eventCoords = playerTab.getByPosition(0, 9);
					EventTimer eventTimer = utilities.getHCF().getTimerManager().getEventTimer();
					EventFaction eventFaction = eventTimer.getEventFaction();
					if (eventFaction instanceof KothFaction)
					{
						KothFaction kothFaction = (KothFaction) eventFaction;
						int x = kothFaction.getCaptureZone().getCuboid().getMaximumPoint().getBlockX();
						int z = kothFaction.getCaptureZone().getCuboid().getMinimumPoint().getBlockZ();

						eventName.text(new ColorUtils().translateFromString("&4" + kothFaction.getName())).send();
						eventTime.text(new ColorUtils().translateFromString("&e" + TimeUtils.LongCountdown.setFormat(eventTimer.getRemaining()))).send();
						eventCoords.text(new ColorUtils().translateFromString("&e" + x + ", " + z)).send();
					}
					else if (eventFaction instanceof ConquestFaction)
					{
						ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
						eventName.text(new ColorUtils().translateFromString("&4 " + conquestFaction.getName())).send();
						eventTime.text(new ColorUtils().translateFromString("&c " + TimeUtils.LongCountdown.setFormat(conquestFaction.getRed().getRemainingCaptureMillis()) + "&b " + TimeUtils.LongCountdown.setFormat(conquestFaction.getBlue().getRemainingCaptureMillis()))).send();
						eventCoords.text(new ColorUtils().translateFromString("&e " + TimeUtils.LongCountdown.setFormat(conquestFaction.getYellow().getRemainingCaptureMillis()) + "&a " + TimeUtils.LongCountdown.setFormat(conquestFaction.getGreen().getRemainingCaptureMillis()))).send();
					}
					else
					{
						eventName.text(ChatColor.RESET.toString()).send();
						eventTime.text(ChatColor.RESET.toString()).send();
						eventCoords.text(ChatColor.RESET.toString()).send();
					}

					playerTab.getByPosition(1, 0).text(new ColorUtils().translateFromString("    &6&lWenjaPvP")).send();

					playerTab.getByPosition(2, 0).text(new ColorUtils().translateFromString("&6&lMap Kit:")).send();
					playerTab.getByPosition(2, 1).text(new ColorUtils().translateFromString("&7Sharpness: 1")).send();
					playerTab.getByPosition(2, 2).text(new ColorUtils().translateFromString("&7Protection: 1")).send();

					playerTab.getByPosition(2, 4).text(new ColorUtils().translateFromString("&6&lWorld Border:")).send();
					playerTab.getByPosition(2, 5).text(new ColorUtils().translateFromString("&72500, 2500")).send();

					playerTab.getByPosition(2, 7).text(new ColorUtils().translateFromString("&6&lEnd Portal:")).send();
					playerTab.getByPosition(2, 8).text(new ColorUtils().translateFromString("&71000, 1000")).send();
					playerTab.getByPosition(2, 9).text(new ColorUtils().translateFromString("&7In each quadrant.")).send();
				}
			}
			else
			{
				cancel();
			}
		}
	}
}