package support.evocative.profile;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import support.evocative.ComboFFA;

import java.util.UUID;

/**
 * Created by Owner on 31/08/2017.
 */
public class Profile implements Listener {

    /**
     * Store in databases
     *
     * @param e
     */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!ComboFFA.getInstance().getConfig().contains("Players." + uuid.toString())) {
            ComboFFA.getInstance().getConfig().set("Players." + uuid.toString() + ".kills", 0);
            ComboFFA.getInstance().saveConfig();
        }
    }

    @EventHandler
    public void onPalyerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (player.getKiller() instanceof Player) {
            Player k = player.getKiller();
            String pUUID = player.getUniqueId().toString();
            String kUUID = k.getUniqueId().toString();
            int kills = ComboFFA.getInstance().getConfig().getInt("Players." + kUUID + ".kills");

            ComboFFA.getInstance().getConfig().set("Players." + kUUID + ".kills", kills + 1);
            ComboFFA.getInstance().getConfig().set("Players." + kUUID + ".deaths", kills + 1);
            ComboFFA.getInstance().saveConfig();
            k.sendMessage(ChatColor.RED + "Your kill has been saved :)");
        }
    }
}

