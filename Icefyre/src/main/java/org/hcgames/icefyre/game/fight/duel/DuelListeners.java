package org.hcgames.icefyre.game.fight.duel;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;

import java.util.HashSet;
import java.util.UUID;

public class DuelListeners implements Listener {

    private Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile != null) {
            DuelRequest duelRequest = profile.getMostRecentDuelRequest();
            if (duelRequest != null) {
                if (duelRequest.getStage() == DuelRequestStage.SELECTING_LADDER && duelRequest.getLadder() == null || duelRequest.getStage() == DuelRequestStage.SELECTING_ARENA && duelRequest.getArena() == null) {
                    player.sendMessage(main.getLangFile().getString("DUEL.COMMAND.CANCELLED"));

                    for (UUID uuid : new HashSet<>(profile.getDuelRequests().keySet())) {
                        if (profile.getDuelRequests().get(uuid).equals(duelRequest)) {
                            profile.getDuelRequests().remove(uuid);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack != null && itemStack.getType() != Material.AIR && profile != null) {
            if (event.getClickedInventory().getTitle().equalsIgnoreCase(DuelRequest.getLadderInventory().getTitle())) {
                DuelRequest duelRequest = profile.getMostRecentDuelRequest();

                if (duelRequest != null) {
                    for (Queue queue : Queue.getQueues()) {
                        if (queue.getLadder().getIcon().getType() == itemStack.getType() && queue.getLadder().getIcon().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()) && queue.getLadder().getIcon().getDurability() == itemStack.getDurability()) {
                            duelRequest.setLadder(queue.getLadder());
                            player.openInventory(DuelRequest.getArenaInventory(duelRequest.getLadder()));
                            duelRequest.setStage(DuelRequestStage.SELECTING_ARENA);
                            break;
                        }
                    }
                }

                event.setCancelled(true);
                return;
            }

            DuelRequest duelRequest = profile.getMostRecentDuelRequest();
            if (duelRequest != null && duelRequest.getLadder() != null) {
                Ladder ladder = duelRequest.getLadder();
                if (event.getClickedInventory().getTitle().equalsIgnoreCase(DuelRequest.getArenaInventory(ladder).getTitle())) {

                    for (Arena arena : ladder.getArenas()) {
                        if (itemStack.isSimilar(arena.getIcon())) {
                            duelRequest.setArena(arena);

                            Player recipient = profile.getPlayerByDuelRequest(duelRequest);
                            if (recipient != null) {
                                Profile recipientProfile = Profile.getByPlayer(recipient);
                                if (recipientProfile != null) {
                                    Party party = Party.getByPlayer(player);

                                    if (party == null && duelRequest.isParty()) {
                                        profile.getDuelRequests().remove(recipient.getUniqueId());
                                        player.sendMessage(main.getLangFile().getString("PARTY.NOT_IN_PARTY").replace("%PLAYER%", player.getName()));
                                        return;
                                    }

                                    Party recipientParty = recipientProfile.getParty();
                                    if (duelRequest.isParty() && recipientParty == null) {
                                        profile.getDuelRequests().remove(recipient.getUniqueId());
                                        player.sendMessage(main.getLangFile().getString("PARTY.NOT_IN_PARTY_OTHER").replace("%PLAYER%", recipient.getName()));
                                        return;
                                    }

                                    Queue queue = Queue.getByPlayer(player);
                                    if (queue != null) {
                                        queue.remove(profile);
                                    }

                                    if (duelRequest.isParty() && party != null) {
                                        party.sendMessage(main.getLangFile().getString("PARTY.SENT").replace("%PLAYER%", recipient.getName()).replace("%LADDER%", duelRequest.getLadder().getName()).replace("%ARENA", arena.getName()));
                                        for (Profile member : recipientParty.getMembers()) {
                                            member.getPlayer().sendMessage(main.getLangFile().getString("PARTY.RECEIVED").replace("%PLAYER%", player.getName()).replace("%LADDER%", duelRequest.getLadder().getName()).replace("%ARENA", arena.getName()));
                                        }
                                        new FancyMessage(main.getLangFile().getString("PARTY.RECEIVED").replace("%PLAYER%", player.getName()).replace("%LADDER%", ladder.getName()).replace("%ARENA%", arena.getName())).command("/duelaccept " + player.getName()).tooltip(main.getLangFile().getString("PARTY.RECEIVED_TOOLTIP")).send(recipient);
                                    } else {
                                        player.sendMessage(main.getLangFile().getString("DUEL.COMMAND.SENT").replace("%PLAYER%", recipient.getName()));
                                        new FancyMessage(main.getLangFile().getString("DUEL.COMMAND.RECEIVED").replace("%PLAYER%", player.getName()).replace("%LADDER%", ladder.getName()).replace("%ARENA%", arena.getName())).command("/duelaccept " + player.getName()).tooltip(main.getLangFile().getString("DUEL.COMMAND.RECEIVED_TOOLTIP")).send(recipient);
                                    }
                                }
                            } else {
                                UUID uuid = profile.getUuidByDuelRequest(duelRequest);
                                if (uuid != null) {
                                    profile.getDuelRequests().remove(uuid); // rip memory leak gang
                                    player.sendMessage(main.getLangFile().getString("DUEL.COMMAND.INVALID_PLAYER").replace("%NAME%", Bukkit.getOfflinePlayer(uuid).getName()));
                                }
                            }

                            player.closeInventory();
                        }
                    }

                    event.setCancelled(true);
                }
            }
        }
    }

}
