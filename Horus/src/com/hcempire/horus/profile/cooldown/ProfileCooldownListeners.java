package com.hcempire.horus.profile.cooldown;

import com.hcempire.horus.Horus;
import com.hcempire.horus.profile.Profile;
import com.hcempire.horus.profile.kit.ProfileKitCooldown;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileCooldownListeners implements Listener {

    @EventHandler
    public void onPlayerInteractEnderpearlEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (player.getGameMode() == GameMode.CREATIVE) return;

                ProfileCooldown cooldown = profile.getCooldownByType(ProfileCooldownType.ENDER_PEARL);
                if (cooldown != null) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(cooldown.getType().getMessage().replace("%TIME%", cooldown.getTimeLeft()));
                    return;
                }

                profile.getCooldowns().add(new ProfileCooldown(ProfileCooldownType.ENDER_PEARL, ProfileCooldownType.ENDER_PEARL.getDuration()));
                profile.setPearlLocation(player.getLocation());
            }

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Profile.getByPlayer(event.getEntity()).getCooldowns().clear();
    }

}
