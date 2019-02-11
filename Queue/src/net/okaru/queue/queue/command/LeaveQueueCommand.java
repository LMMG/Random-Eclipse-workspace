package net.okaru.queue.queue.command;

import net.okaru.queue.oQueue;
import net.okaru.queue.queue.Queue;
import net.okaru.queue.util.command.BaseCommand;
import net.okaru.queue.util.command.Command;
import net.okaru.queue.util.command.CommandArgs;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveQueueCommand
extends BaseCommand {
    public LeaveQueueCommand(oQueue main) {
        super(main);
    }

    @Command(name="leavequeue")
    @Override
    public void onCommand(CommandArgs command) {
        final Player player = command.getPlayer();
        final Queue queue = Queue.getByPlayer(player);
        if (queue == null) {
            player.sendMessage(this.configFile.getString("MESSAGES.NOT_IN_QUEUE"));
            return;
        }
        player.sendMessage(this.configFile.getString("MESSAGES.LEFT_QUEUE").replace("%QUEUE%", queue.getName()));
        new BukkitRunnable(){

            @Override
			public void run() {
                queue.removePlayer(player, true);
            }
        }.runTaskAsynchronously(this.main);
    }

}

