package com.hcempire.horus.event.koth.command;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.event.Event;
import com.hcempire.horus.event.EventManager;
import com.hcempire.horus.event.koth.KothEvent;
import com.hcempire.horus.util.DateUtil;
import com.hcempire.horus.util.HorusCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class KothStopCommand extends HorusCommand {

    private static final long DEFAULT_DURATION = 900000;

    @Command(name = "koth.stop", permission = "koth.stop")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/koth stop <koth>");
            return;
        }

        Event event = EventManager.getInstance().getByName(args[0]);

        if (event == null || (!(event instanceof KothEvent))) {
            sender.sendMessage(ChatColor.RED + "Please specify a valid KoTH.");
            return;
        }

        KothEvent koth = (KothEvent) event;

        if (!(koth.isActive())) {
            sender.sendMessage(ChatColor.RED + "KoTH " + koth.getName() + " isn't active!");
            return;
        }

        koth.stop(true);
    }
}
