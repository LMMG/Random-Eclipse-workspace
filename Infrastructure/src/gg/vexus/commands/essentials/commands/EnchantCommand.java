package gg.vexus.commands.essentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

public class EnchantCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("enchant")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return true;
            }
            if (!Essentials.hasPermission(sender, "enchant")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /enchant <enchantment> <level>");
                return true;
            }
            if (args.length == 1) {
                p.sendMessage(ChatColor.RED + "Usage: /enchant <enchantment> <level>");
                return true;
            }
            if (args.length == 2) {
                if (p.getItemInHand() == null) {
                    p.sendMessage(ChatColor.RED + "You are not holding an item!");
                    return true;
                }
                if (Ints.tryParse(args[1]) == null) {
                    p.sendMessage(ChatColor.RED + "That is not a valid number.");
                    return true;
                }
                final int level = Ints.tryParse(args[1]);
                final Enchantment type = Enchantment.getByName(args[0].toUpperCase());
                if (type == null && this.getEnchant(args[0]) == null) {
                    p.sendMessage(ChatColor.RED + "That is not a valid enchantment type.");
                    return true;
                }
                if (type != null) {
                    p.getItemInHand().addUnsafeEnchantment(type, level);
                    p.sendMessage(ChatColor.BLUE + "Added " + type.getName() + " " + ChatColor.GRAY + "(" + level + ")" + ChatColor.BLUE + " to your " + WordUtils.capitalizeFully(p.getItemInHand().getType().name().replace("_", " ")) + ".");
                    return true;
                }
                if (type == null && this.getEnchant(args[0]) != null) {
                    p.getItemInHand().addUnsafeEnchantment(this.getEnchant(args[0]), level);
                    p.sendMessage(ChatColor.BLUE + "Added " + this.getEnchant(args[0]).getName() + " " + ChatColor.GRAY + "(" + level + ")" + ChatColor.BLUE + " to your " + WordUtils.capitalizeFully(p.getItemInHand().getType().name().replace("_", " ")) + ".");
                    return true;
                }
            }
        }
        return false;
    }
    
    public Enchantment getEnchant(final String s) {
        if (s.equalsIgnoreCase("sharpness")) {
            return Enchantment.DAMAGE_ALL;
        }
        if (s.equalsIgnoreCase("protection")) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        if (s.equalsIgnoreCase("unbreaking")) {
            return Enchantment.DURABILITY;
        }
        if (s.equalsIgnoreCase("efficiency")) {
            return Enchantment.DIG_SPEED;
        }
        if (s.equalsIgnoreCase("fortune")) {
            return Enchantment.LOOT_BONUS_BLOCKS;
        }
        if (s.equalsIgnoreCase("looting")) {
            return Enchantment.LOOT_BONUS_MOBS;
        }
        return null;
    }
}