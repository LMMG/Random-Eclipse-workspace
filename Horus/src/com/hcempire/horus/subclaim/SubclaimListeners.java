package com.hcempire.horus.subclaim;

import com.alexandeh.ekko.factions.claims.Claim;
import com.alexandeh.ekko.profiles.Profile;
import com.hcempire.horus.Horus;
import com.hcempire.horus.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;

public class SubclaimListeners implements Listener {

    private static Horus main = Horus.getInstance();

    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent event) {
        if (event.getSource() instanceof Chest) {
            Chest chest = (Chest) event.getSource().getHolder();
            Subclaim subclaim = Subclaim.getByBlock(chest.getBlock());

            if (subclaim != null) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getDestination() instanceof Chest) {
            Chest chest = (Chest) event.getDestination().getHolder();
            Subclaim subclaim = Subclaim.getByBlock(chest.getBlock());

            if (subclaim != null) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Subclaim subclaim = Subclaim.getByBlock(block);

            if (subclaim != null && !subclaim.isAllowed(event.getPlayer())) {
                event.getPlayer().sendMessage(main.getLangFile().getString("SUBCLAIM.NOT_ALLOWED"));
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (event.getBlock().equals(Material.SIGN)) {
            if (!profile.getFaction().getOfficers().contains(player.getUniqueId()) && !profile.getFaction().getLeader().equals(player.getUniqueId())) {
                event.setCancelled(true);
                player.sendMessage(Color.translate("&cYou don't have permission to break that subclaimed sign."));
            }
        }
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (event.getLine(0).equalsIgnoreCase(main.getConfigFile().getString("SUBCLAIM.CREATE_TEXT"))) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile != null) {
                Sign sign = (Sign) event.getBlock().getState();
                org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();

                BlockFace face = signData.getAttachedFace();

                if (profile.getFaction() == null) {
                    player.sendMessage(main.getLangFile().getString("SUBCLAIM.NOT_IN_FACTION"));
                    return;
                }

                if (!profile.getFaction().getLeader().equals(player.getUniqueId()) && !profile.getFaction().getOfficers().contains(player.getUniqueId())) {
                    player.sendMessage(main.getLangFile().getString("SUBCLAIM.NOT_OFFICER"));
                    return;
                }

                Claim claim = Claim.getProminentClaimAt(sign.getBlock().getRelative(face).getLocation());
                if (claim != null && claim.getFaction().equals(profile.getFaction())) {

                    if (!(Arrays.asList(Subclaim.FACES)).contains(face) || !(sign.getBlock().getRelative(face).getState() instanceof InventoryHolder)) {
                        player.sendMessage(main.getLangFile().getString("SUBCLAIM.INVALID_BLOCK"));
                        return;
                    }

                    if (Subclaim.getByBlock(sign.getBlock().getRelative(face)) != null) {
                        player.sendMessage(main.getLangFile().getString("SUBCLAIM.ALREADY_SUBCLAIMED"));
                        return;
                    }

                    event.setLine(0, main.getLangFile().getString("SUBCLAIM.SIGN_TEXT"));
                    player.sendMessage(main.getLangFile().getString("SUBCLAIM.CREATED"));
                } else {
                    player.sendMessage(main.getLangFile().getString("SUBCLAIM.NOT_IN_CLAIM"));
                }
            }
        }
    }

}
