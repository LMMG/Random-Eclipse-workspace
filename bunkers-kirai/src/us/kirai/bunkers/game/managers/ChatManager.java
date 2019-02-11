package us.kirai.bunkers.game.managers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import us.kirai.bunkers.Bunkers;
import us.kirai.bunkers.game.Team;

public class ChatManager implements Listener
{
    private ArrayList<String> teamChat;
    
    public ChatManager() {
        this.teamChat = new ArrayList<String>();
    }
    
    public boolean isTeamChat(final Player p) {
        return this.teamChat.contains(p.getName());
    }
    
    public void setTeamChat(final Player p, final boolean tc) {
        if (this.teamChat.contains(p.getName())) {
            this.teamChat.remove(p.getName());
        }
        if (tc) {
            this.teamChat.add(p.getName());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void chat(final AsyncPlayerChatEvent e) {
        if (this.isTeamChat(e.getPlayer())) {
            e.setCancelled(true);
            for (final String s : Bunkers.getInstance().getGameHandler().getPlayers().keySet()) {
                final Player p = Bukkit.getPlayer(s);
                if (p != null) {
                    final Team t = Bunkers.getInstance().getGameHandler().getPlayers().get(s);
                    if (t == null || !t.equals(Bunkers.getInstance().getGameHandler().getPlayers().get(e.getPlayer().getName()))) {
                        continue;
                    }
                    p.sendMessage(String.valueOf(t.getColor().toString()) + "[TEAM] " + t.toString() + ": " + e.getPlayer().getName() + " - §7" + e.getMessage());
                }
            }
            return;
        }
        e.setFormat(String.valueOf(Bunkers.getInstance().getGameHandler().getPlayers().get(e.getPlayer().getName()).getColor().toString()) + "[" + Bunkers.getInstance().getGameHandler().getPlayers().get(e.getPlayer().getName()).toString() + "] " + "§r" + PermissionsEx.getUser(e.getPlayer()).getPrefix() + ((PermissionsEx.getUser(e.getPlayer()).getPrefix() == "") ? "" : " ") + e.getPlayer().getDisplayName() + "§7: " + "§r" + e.getMessage());
    }
}
