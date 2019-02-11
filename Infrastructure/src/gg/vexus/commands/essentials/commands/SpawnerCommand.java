package gg.vexus.commands.essentials.commands;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import gg.vexus.commands.essentials.Essentials;
import gg.vexus.utils.StringRegister;

public class SpawnerCommand implements CommandExecutor
{
    @SuppressWarnings("unchecked")
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawner")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a player.");
                return true;
            }
            if (!Essentials.hasPermission(sender, "spawner")) {
                sender.sendMessage(StringRegister.PERMISSION_MESSAGE);
                return true;
            }
            final Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Usage: /spawner <type>");
                return true;
            }
            if (args.length == 1) {
                if (p.getTargetBlock((HashSet)null, 0).getType() != Material.MOB_SPAWNER) {
                    p.sendMessage(ChatColor.RED + "You are not looking at a spawner.");
                    return true;
                }
                EntityType type;
                try {
                    type = EntityType.valueOf(args[0].toUpperCase());
                }
                catch (Exception e) {
                    p.sendMessage(ChatColor.RED + args[0] + " is not a valid mob type.");
                    return true;
                }
                final CreatureSpawner spawner = (CreatureSpawner)p.getTargetBlock((HashSet)null, 0).getState();
                spawner.setSpawnedType(type);
                spawner.update();
                p.sendMessage(ChatColor.BLUE + "Spawner type changed to " + type.getName() + ".");
                return true;
            }
        }
        return false;
    }
}