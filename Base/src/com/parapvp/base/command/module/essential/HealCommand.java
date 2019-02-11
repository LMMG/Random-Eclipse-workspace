package com.parapvp.base.command.module.essential;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.parapvp.util.BukkitUtils;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.potion.PotionEffectType;
import java.util.Set;
import com.parapvp.base.command.BaseCommand;
import com.parapvp.base.util.BaseConstants;

public class HealCommand extends BaseCommand
{
    private static final Set<PotionEffectType> HEALING_REMOVEABLE_POTION_EFFECTS;
    
    public HealCommand() {
        super("heal", "Heals a player.");
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player onlyTarget = null;
        Collection<Player> targets;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")) {
                targets = (Collection<Player>)ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
            else {
                if ((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null || !BaseCommand.canSee(sender, onlyTarget)) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
                return true;
            }
            targets = ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
        }
        final double maxHealth;
        if (onlyTarget != null && (maxHealth = ((CraftPlayer)onlyTarget).getHealth()) == ((CraftPlayer)onlyTarget).getMaxHealth()) {
            sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full health (" + maxHealth + ").");
            return true;
        }
        for (final Player target : targets) {
            target.setHealth(((CraftPlayer)target).getMaxHealth());
            for (final PotionEffectType type : HealCommand.HEALING_REMOVEABLE_POTION_EFFECTS) {
                target.removePotionEffect(type);
            }
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Healed " + ((onlyTarget == null) ? "all online players" : ("player " + onlyTarget.getName())) + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    static {
        HEALING_REMOVEABLE_POTION_EFFECTS = (Set)ImmutableSet.of((Object)PotionEffectType.SLOW, (Object)PotionEffectType.SLOW_DIGGING, (Object)PotionEffectType.POISON, (Object)PotionEffectType.WEAKNESS);
    }
}