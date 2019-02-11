package us.kirai.bunkers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.kirai.bunkers.config.ConfigurationService;
import us.kirai.bunkers.utils.BroadcastUtils;

public class LobbySpawn implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c§lPLAYERS ONLY!");
            return true;
        }
        final Player player = (Player)sender;
        if (!player.hasPermission("uhcg.start") && !player.isOp()) {
            player.sendMessage("§c§lNO PERMISSIONS...");
            return true;
        }
        if (args.length != 0) {
            player.sendMessage("§cCorrect usage: " + command.getUsage());
            return true;
        }
        ConfigurationService.setLobbySpawn(player.getLocation());
        BroadcastUtils.broadcastToPerm("§7[" + player.getName() + ": §oSet the lobby spawn to " + (int)player.getLocation().getX() + "x, " + (int)player.getLocation().getY() + "y, " + (int)player.getLocation().getZ() + "z§7]", "uhcb.setlobbyspawn");
        return true;
    }
}
