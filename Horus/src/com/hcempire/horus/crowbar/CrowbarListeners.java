package com.hcempire.horus.crowbar;

import com.alexandeh.ekko.Ekko;
import com.alexandeh.ekko.factions.Faction;
import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.factions.type.SystemFaction;
import com.alexandeh.ekko.profiles.Profile;
import com.alexandeh.ekko.utils.ItemBuilder;
import com.hcempire.horus.Horus;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CrowbarListeners implements Listener {

    private static Horus main = Horus.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            Crowbar crowbar = Crowbar.getByItemStack(event.getItem());

            if (crowbar == null) {
                return;
            }

            event.setCancelled(true);

            if (block.getType() == Material.ENDER_PORTAL_FRAME || block.getType() == Material.MOB_SPAWNER) {
                boolean spawner = block.getType() == Material.MOB_SPAWNER;

                if (spawner && crowbar.getSpawnerUses() <= 0) {
                    player.sendMessage(main.getLangFile().getString("CROWBAR.NO_SPAWNER_USES"));
                    return;
                }

                if (!spawner && crowbar.getPortalUses() <= 0) {
                    player.sendMessage(main.getLangFile().getString("CROWBAR.NO_PORTAL_USES"));
                    return;
                }

                Profile ekkoProfile = Profile.getByUuid(player.getUniqueId());
                PlayerFaction playerFaction = ekkoProfile.getFaction();
                Location location = block.getLocation();
                Claim claim = Claim.getProminentClaimAt(location);

                if (claim != null && !ekkoProfile.isInAdminMode()) {
                    Faction faction = claim.getFaction();
                    if (faction != null && (playerFaction == null || !faction.equals(playerFaction))) {
                        if (faction instanceof SystemFaction) {
                            SystemFaction systemFaction = (SystemFaction) faction;
                            player.sendMessage(Ekko.getInstance().getLangConfig().getString("FACTION_CLAIM.CANNOT_BUILD_SYSTEM").replace("%COLOR%", systemFaction.getColor() + "").replace("%FACTION%", systemFaction.getName()));
                            return;
                        } else {
                            if (faction instanceof PlayerFaction && playerFaction != null && playerFaction.getAllies().contains(faction)) {
                                player.sendMessage(Ekko.getInstance().getLangConfig().getString("FACTION_CLAIM.CANNOT_BUILD_ALLY").replace("%FACTION%", faction.getName()));
                                return;
                            } else {
                                player.sendMessage(Ekko.getInstance().getLangConfig().getString("FACTION_CLAIM.CANNOT_BUILD_ENEMY").replace("%FACTION%", faction.getName()));
                                return;
                            }
                        }

                    }
                }

                ItemStack toDrop;

                if (spawner) {
                    crowbar.setSpawnerUses(crowbar.getSpawnerUses() - 1).update();
                    CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
                    toDrop = new ItemBuilder(Material.MOB_SPAWNER).name(ChatColor.GREEN + creatureSpawner.getCreatureTypeName() + " Spawner").build();
                } else {
                    crowbar.setPortalUses(crowbar.getPortalUses() - 1).update();
                    toDrop = new ItemStack(block.getType());
                }

                block.getWorld().dropItemNaturally(block.getLocation(), toDrop);
                block.setType(Material.AIR);
                player.updateInventory();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (itemStack.getType() == Material.MOB_SPAWNER) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                String name = itemStack.getItemMeta().getDisplayName();
                if (name.startsWith(ChatColor.GREEN.toString())) {
                    ((CreatureSpawner)event.getBlock().getState()).setCreatureTypeByName(ChatColor.stripColor(name.replace(" Spawner", "")));
                }
            }
        }
    }

}
