package net.nersa.lobby.server;

import net.md_5.bungee.api.ChatColor;
import net.nersa.lobby.Lobby;
import net.nersa.lobby.selector.ServerSelector;

import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class PlayerEvents implements Listener {


    public static void spawn(World world, Player p) {
        Bukkit.dispatchCommand(p, "spawn");
        p.sendMessage(ChatColor.GREEN + "You have been teleported to spawn!");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (Item item : this.epItems) {
            if (item.getPassenger().equals(p)) {
                item.eject();
            }
        }

        p.getInventory().clear();
        spawn(p.getWorld(), p);
        p.setWalkSpeed(0.5F);
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.getInventory().setItem(4, ServerSelector.selector);
    }

    @EventHandler
    public void onJoin123(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "    &7Welcome to  the &b&lWenjaPvP Network"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &b» &bWebsite&7: &7www.wenjapvp.net"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &b» &bTeamspeak&7: &7ts.wenjapvp.net"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &b» &bStore&7: &7store.wenjapvp.net"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &b» &bTwitter&7: &7@WenjaPvP"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m------------------------------------------"));
    }

    @EventHandler
    public void onTrhowEnder(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if ((!event.hasItem()) || (!event.getItem().getType().equals(Material.ENDER_PEARL)) || ((!action.equals(Action.RIGHT_CLICK_AIR)) && (!action.equals(Action.RIGHT_CLICK_BLOCK)))) {
            return;
        }
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
        event.setUseInteractedBlock(Event.Result.DENY);
        Item ep = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), ItemStackBuilder.get(Material.ENDER_PEARL, 1, (short) 0, UUID.randomUUID().toString(), null));
        ep.setPickupDelay(10000);
        ep.setVelocity(player.getLocation().getDirection().normalize().multiply(2.0F));
        ep.setPassenger(player);
        player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        deleteItemWhenNeeded(ep);
        player.updateInventory();
    }

    Set<Item> epItems = new HashSet();

    public void deleteItemWhenNeeded(final Item item) {
        new BukkitRunnable() {
            public void run() {
                if (item.isDead()) {
                    cancel();
                }
                if ((item.getVelocity().getX() == 0.0D) || (item.getVelocity().getY() == 0.0D) || (item.getVelocity().getZ() == 0.0D)) {
                    Player p = (Player) item.getPassenger();
                    PlayerEvents.this.epItems.remove(item);
                    item.remove();
                    if (p != null) {
                        p.teleport(p.getLocation().add(0.0D, 0.5D, 0.0D));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(Lobby.getInstance(), 2L, 1L);
    }

    @EventHandler
    public void handleEntityDamageByDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if ((event.getEntity() instanceof Player)) {
                Player damagee = (Player) event.getEntity();
                damager.hidePlayer(damagee);
                damager.sendMessage(ChatColor.AQUA + "Pop...");
            }
        }
    }
}