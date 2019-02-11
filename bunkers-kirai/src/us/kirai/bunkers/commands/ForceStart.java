package us.kirai.bunkers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.kirai.bunkers.Bunkers;
import us.kirai.bunkers.game.GameState;
import us.kirai.bunkers.utils.BroadcastUtils;

public class ForceStart implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c§lPLAYERS ONLY!");
            return true;
        }
        final Player player = (Player)sender;
        if (!(player.hasPermission("uhcg.start")) && !player.isOp()) {
            player.sendMessage("§c§lNO PERMISSIONS...");
            return true;
        }
        if (args.length != 0) {
            player.sendMessage("§cCorrect usage: " + command.getUsage());
            return true;
        }
        if (!Bunkers.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            player.sendMessage("§cThe game is already running!");
            return true;
        }
        Bunkers.getInstance().getGameHandler().startGame();
        BroadcastUtils.broadcastToPerm("§7[" + player.getName() + ": §oForce started the game.§7]", "uhcb.forcestart");
        return true;
    }
}
