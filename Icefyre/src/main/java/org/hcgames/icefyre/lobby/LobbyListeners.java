package org.hcgames.icefyre.lobby;

import org.bukkit.event.player.*;
import org.hcgames.icefyre.game.kit.builder.KitBuilder;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.game.queue.QueueProfile;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.party.PartyHandler;
import org.hcgames.icefyre.util.file.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbyListeners implements Listener {

    private Lobby lobby;
    private PartyHandler partyHandler;
    private ConfigFile configFile;

    public LobbyListeners(Lobby lobby) {
        this.lobby = lobby;
        this.partyHandler = lobby.getMain().getPartyHandler();
        this.configFile = lobby.getConfigFile();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        lobby.setupPlayer(event.getPlayer());
        lobby.teleportToSpawn(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (lobby.isInside(event.getPlayer())) {
            Profile profile = Profile.getByPlayer(event.getPlayer());
            if (profile != null) {
                if (profile.getBuilderProfile() != null) {
                    event.getItemDrop().remove();
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
        if (lobby.isInside(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (lobby.isInside(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onInventoryInteractEvent(InventoryInteractEvent event) {
        if (lobby.isInside((Player) event.getWhoClicked())) {
            Profile profile = Profile.getByPlayer((Player) event.getWhoClicked());
            if (profile != null && profile.getBuilderProfile() == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (lobby.isInside((Player) event.getWhoClicked())) {
            Profile profile = Profile.getByPlayer((Player) event.getWhoClicked());
            if (profile != null && profile.getBuilderProfile() == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && lobby.isInside((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (lobby.isInside(player)) {
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (lobby.isInside(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onPlayerInteractRankedQueueItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.RANKED.ITEM")), event.getAction());
        if (profile != null && (profile.getParty() == null || (profile.getParty() != null && !profile.getParty().getLeader().getPlayer().getName().equals(event.getPlayer().getName())))) {
            event.getPlayer().openInventory(Queue.getGraphicalInterface(QueueType.RANKED));
        }
    }

    @EventHandler
    public void onPlayerInteractUnrankedQueueItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.UNRANKED.ITEM")), event.getAction());

        if (profile != null && (profile.getParty() == null || (profile.getParty() != null && !profile.getParty().getLeader().getPlayer().getName().equals(event.getPlayer().getName())))) {
            event.getPlayer().openInventory(Queue.getGraphicalInterface(QueueType.UNRANKED));
        }
    }

    @EventHandler
    public void onPlayerInteractListTeamItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.LIST.ITEM")), event.getAction());;
        if (profile != null && profile.getParty() != null && profile.getParty().getLeader().equals(profile)) {
            partyHandler.sendTeamPlayerListMessage(profile.getPlayer(), profile.getParty());
        }
    }

    @EventHandler
    public void onPlayerInteractTeamFightEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.FIGHT.ITEM")), event.getAction());;
        if (profile != null && profile.getParty() != null && profile.getParty().getLeader().equals(profile)) {
            event.getPlayer().openInventory(Party.getPartyInventory(1));
        }
    }

    @EventHandler
    public void onPlayerInteractLeaveQueueItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.LEAVE_QUEUE.ITEM")), event.getAction());
        if (profile != null && profile.getQueueProfile() != null) {
            profile.getQueueProfile().getQueue().remove(profile);
            profile.getPlayer().sendMessage(lobby.getLangFile().getString("QUEUE.LEAVE"));
        }
    }

    @EventHandler
    public void onPlayerInteractQueueInformationItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.QUEUE.NO_TEAM.QUEUE_INFO.ITEM")), event.getAction());
        if (profile != null && profile.getQueueProfile() != null) {
            QueueProfile queueProfile = profile.getQueueProfile();
            List<String> toSend = lobby.getLangFile().getStringList("QUEUE.INFORMATION." + (queueProfile.getQueue().getType() == QueueType.RANKED ? "RANKED" : "UNRANKED"));

            for (String string : toSend) {
                string = string.replace("%ELO%", queueProfile.getRanking() + "");
                string = string.replace("%LADDER%", queueProfile.getQueue().getLadder().getName());

                if (queueProfile.getRange()[0] >= 0) {
                    string = string.replace("%LOW_RANGE%", queueProfile.getRanking() - queueProfile.getRange()[0] + "");
                    string = string.replace("%HIGH_RANGE%", queueProfile.getRanking() + queueProfile.getRange()[0] + "");
                }

                profile.getPlayer().sendMessage(string);
            }

        }
    }

    @EventHandler
    public void onPlayerInteractDeleteTeamItemEvent(PlayerInteractEvent event) {
        Profile profile = isValidInteract(event.getPlayer(), event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.DELETE.ITEM")), event.getAction());
        if (profile != null && profile.getParty() != null && profile.getParty().getLeader().equals(profile)) {
            Party party = profile.getParty();
            Party.getParties().remove(party);

            party.getLeader().getInvitedPlayers().clear();
            party.sendMessage(lobby.getLangFile().getString("PARTY.DELETED").replace("%PLAYER%", party.getLeader().getName()));
            party.getMembers().add(party.getLeader());
            for (Profile members : party.getMembers()) {
                lobby.setupPlayer(members.getPlayer());
                lobby.getMain().getFightHandler().setupPlayer(members.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractKitBuilderItemEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = isValidInteract(player, event.getItem(), Material.valueOf(configFile.getString("HOTBAR_ITEMS.NO_TEAM.KIT_BUILDER.ITEM")), event.getAction());
        if (profile != null && (profile.getParty() == null || (profile.getParty() != null && !profile.getParty().getLeader().getPlayer().getName().equals(event.getPlayer().getName())))) {
            if (profile.getQueueProfile() == null) {
               KitBuilder kitBuilder = lobby.getKitBuilder();

                if (kitBuilder.getLocation() == null) {
                    event.getPlayer().sendMessage(lobby.getLangFile().getString("ERROR.KIT_BUILDER_NOT_DEFINED"));
                    return;
                }

                player.openInventory(kitBuilder.getGraphicalInterface(QueueType.UNRANKED));
            }
        }
    }

    //TODO: Rename this ugly shit lol tf
    private Profile isValidInteract(Player player, ItemStack itemStack, Material material, Action action) {
        if (lobby.isInside(player) && itemStack != null && action.name().contains("RIGHT")) {
            if (itemStack.getType() == material) {
                Profile profile = Profile.getByPlayer(player);
                if (profile != null && profile.getBuilderProfile() == null) {
                    return profile;
                }
            }
        }
        return null;
    }



}
