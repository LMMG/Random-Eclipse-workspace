package net.wenjapvp.kohisg.spectate;

import net.wenjapvp.kohisg.utils.ItemStackUtils;
import net.wenjapvp.kohisg.utils.PlayerUtils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectateManager {
    private final Set<Player> spectator = new HashSet<>();

    public boolean isSpectating(Player player) {
        return spectator.contains(player);
    }

    public void spectate(Player player) {
        if (!spectator.contains(player)) {
            spectator.add(player);
        }

        player.setGameMode(GameMode.CREATIVE);
        player.setCanPickupItems(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        PlayerUtils.clearInventory(player);
        player.getInventory().setItem(0, ItemStackUtils.setItemTitle(new ItemStack(Material.REDSTONE), ItemConstant.NEXT_RANDOM));

        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (spectator.contains(other)) {
                player.showPlayer(other);
                other.showPlayer(player);
            } else {
                player.showPlayer(other);
                other.hidePlayer(player);
            }
        }
    }

    public void unSpectate(Player player) {
        spectator.remove(player);

        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.setCanPickupItems(true);
        player.setGameMode(GameMode.SURVIVAL);
        PlayerUtils.clearInventory(player);

        for (Player other : Bukkit.getServer().getOnlinePlayers()) {
            if (spectator.contains(other)) {
                player.hidePlayer(other);
                other.hidePlayer(player);
            } else {
                player.showPlayer(other);
                other.showPlayer(player);
            }
        }
    }
}