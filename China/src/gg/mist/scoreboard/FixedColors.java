package gg.mist.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import server.wenjapvp.hcf.HCF;

public class FixedColors
  implements Listener
{
  @EventHandler
  public void onJoin(PlayerJoinEvent e)
  {
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player all = arrayOfPlayer[i];
      updateTab(all);
    }
  }
  
  public static void updateTab(Player player)
  {
    boolean newScoreboard = false;
    Scoreboard scoreboard;
    Scoreboard scoreboard1;
    if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard())
    {
      scoreboard1 = player.getScoreboard();
    }
    else
    {
      scoreboard1 = Bukkit.getScoreboardManager().getNewScoreboard();
      newScoreboard = true;
    }
    Team friendly = getExistingOrCreateNewTeam(player, "friendly", scoreboard1, ChatColor.DARK_GREEN);
    Team enemy = getExistingOrCreateNewTeam(player, "enemy", scoreboard1, ChatColor.RED);
    Team ally = getExistingOrCreateNewTeam(player, "ally", scoreboard1, ChatColor.BLUE);
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player all = arrayOfPlayer[i];
      if (HCF.getPlugin().getFactionManager().getPlayerFaction(player) == null) {
        enemy.addEntry(all.getName());
      } else if (HCF.getPlugin().getFactionManager().getPlayerFaction(all) == null) {
        enemy.addEntry(all.getName());
      } else if (HCF.getPlugin().getFactionManager().getPlayerFaction(player) == HCF.getPlugin().getFactionManager().getPlayerFaction(all)) {
        friendly.addEntry(all.getName());
      } else if (HCF.getPlugin().getFactionManager().getPlayerFaction(player).getAllied().contains(all.getUniqueId())) {
        ally.addEntry(all.getName());
      } else {
        enemy.addEntry(all.getName());
      }
    }
    if (newScoreboard) {
      player.setScoreboard(scoreboard1);
    }
  }
  
  private static Team getExistingOrCreateNewTeam(Player player, String string, Scoreboard scoreboard, ChatColor prefix)
  {
    Team toReturn = scoreboard.getTeam(string);
    if (toReturn == null)
    {
      toReturn = scoreboard.registerNewTeam(string);
      toReturn.setPrefix(prefix);
      if (string == "friendly") {
        toReturn.setCanSeeFriendlyInvisibles(true);
      }
    }
    return toReturn;
  }
}