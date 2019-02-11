package com.parapvp.base.command.module.essential;

import java.util.Iterator;
import java.util.Collection;
import com.parapvp.util.BukkitUtils;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.Chunk;
import org.bukkit.Location;
import com.google.common.base.Optional;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Player;
import com.google.common.primitives.Ints;
import com.google.common.base.Enums;
import org.bukkit.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.parapvp.base.command.BaseCommand;

public class RemoveEntityCommand extends BaseCommand
{
    public RemoveEntityCommand() {
        super("removeentity", "Removes all of a specific entity.");
        this.setUsage("/(command) <worldName> <entityType> [removeCustomNamed] [radius]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final World world = Bukkit.getWorld(args[0]);
        final Optional<EntityType> optionalType = (Optional<EntityType>)Enums.getIfPresent((Class)EntityType.class, args[1].toUpperCase());
        if (!optionalType.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Not an entity named '" + args[1] + "'.");
            return true;
        }
        final EntityType entityType = (EntityType)optionalType.get();
        if (entityType == EntityType.PLAYER) {
            sender.sendMessage(ChatColor.RED + "You cannot remove " + entityType.name() + " entities!");
            return true;
        }
        final boolean removeCustomNamed = args.length > 2 && Boolean.parseBoolean(args[2]);
        Integer radius;
        if (args.length > 3) {
            radius = Ints.tryParse(args[3]);
            if (radius == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[3] + "' is not a number.");
                return true;
            }
            if (radius <= 0) {
                sender.sendMessage(ChatColor.RED + "Radius must be positive.");
                return true;
            }
        }
        else {
            radius = 0;
        }
        final Location location = (sender instanceof Player) ? ((Player)sender).getLocation() : null;
        int removed = 0;
        for (final Chunk chunk : world.getLoadedChunks()) {
            for (final Entity entity : chunk.getEntities()) {
                Label_0506: {
                    if (entity.getType() == entityType && (radius == 0 || (location != null && location.distanceSquared(entity.getLocation()) <= radius))) {
                        if (!removeCustomNamed) {
                            if (entity instanceof Tameable && ((Tameable)entity).isTamed()) {
                                break Label_0506;
                            }
                            if (entity instanceof LivingEntity) {
                                final LivingEntity livingEntity = (LivingEntity)entity;
                                if (livingEntity.getCustomName() != null) {
                                    break Label_0506;
                                }
                            }
                        }
                        entity.remove();
                        ++removed;
                    }
                }
            }
        }
        sender.sendMessage(ChatColor.YELLOW + "Removed " + removed + " of " + entityType.getName() + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        List<String> results = null;
        switch (args.length) {
            case 1: {
                final Collection<World> worlds = (Collection<World>)Bukkit.getWorlds();
                results = new ArrayList<String>(worlds.size());
                for (final World world : worlds) {
                    results.add(world.getName());
                }
                break;
            }
            case 2: {
                final EntityType[] entityTypes = EntityType.values();
                results = new ArrayList<String>(entityTypes.length);
                for (final EntityType entityType : entityTypes) {
                    if (entityType != EntityType.UNKNOWN && entityType != EntityType.PLAYER) {
                        results.add(entityType.name());
                    }
                }
                break;
            }
            default: {
                return Collections.emptyList();
            }
        }
        return BukkitUtils.getCompletions(args, results);
    }
}