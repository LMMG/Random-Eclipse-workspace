package org.hcgames.icefyre.game.queue;

import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.profile.ProfileState;

public class QueueListeners implements Listener {

    private Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onInventoryClickQueueEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        ItemStack itemStack = event.getCurrentItem();
        if (event.getInventory().getTitle().equalsIgnoreCase(Queue.getGraphicalInterface(event.getInventory().getTitle().equalsIgnoreCase(Queue.getGraphicalInterface(QueueType.RANKED).getTitle()) ? QueueType.RANKED : QueueType.UNRANKED).getTitle())) {
            event.setCancelled(true);
            if (profile != null && profile.getQueueProfile() == null && itemStack != null) {
                for (Queue queue : Queue.getQueues(event.getInventory().getTitle().equalsIgnoreCase(Queue.getGraphicalInterface(QueueType.RANKED).getTitle()) ? QueueType.RANKED : QueueType.UNRANKED)) {
                    if (queue.getLadder().getIcon().getType() == itemStack.getType() && queue.getLadder().getIcon().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()) && queue.getLadder().getIcon().getDurability() == itemStack.getDurability()) {
                        if (queue.getType() == QueueType.RANKED) {
                            if (profile.getUnrankedMatchesWon() < main.getConfigFile().getInt("REQUIRED_UNRANKED_MATCHES")) {
                                player.sendMessage(main.getLangFile().getString("UNRANKED_MATCHES").replace("%AMOUNT%", main.getConfigFile().getInt("REQUIRED_UNRANKED_MATCHES") - profile.getUnrankedMatchesWon() + ""));
                                player.closeInventory();
                                return;
                            }
                        }
                        main.getQueueHandler().addProfileToQueue(profile, queue);
                        profile.setState(ProfileState.IN_QUEUE);
                        player.closeInventory();
                        break;
                    }
                }
            }
        }
    }

}
