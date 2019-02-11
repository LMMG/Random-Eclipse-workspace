package fun.ohvals.yasuo.commands;

import fun.ohvals.yasuo.Yasuo;
import fun.ohvals.yasuo.profile.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class RequestCommand extends Command {

    private Yasuo yasuo = Yasuo.getYasuo();

    private Map<UUID, Long> cd;

    public RequestCommand() {
        super("request", "yasuo.request", "helpop", "halppls", "helpme");
        cd = new HashMap<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        if (!sender.hasPermission("yasuo.request")) {
            sender.sendMessage(yasuo.getLangConfig().getConfig().getString("NO_PERMISSION"));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length < 1) {
            player.sendMessage(yasuo.getLangConfig().getString("USAGE") + "request <reason...>");
            return;
        }

        if (cd.containsKey(player.getUniqueId()) && System.currentTimeMillis()/1000 - cd.get(player.getUniqueId()) <= yasuo.getMainConfig().getInt("REQUEST_COOLDOWN")) {
            player.sendMessage(yasuo.getLangConfig().getString("REQUEST_COOLDOWN"));
            return;
        }

        cd.put(player.getUniqueId(), System.currentTimeMillis()/1000);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            if (!args[i].isEmpty()) {
                sb.append(args[i]);
                if (i + 1 < args.length) {
                    sb.append(" ");
                }
            }
        }

        String request = sb.toString();

        int i = 0;
        for (ProxiedPlayer staff : yasuo.getProxy().getPlayers()) {
            if (staff.hasPermission("yasuo.request.receive")) {
                Profile profile = yasuo.getProfileManager().getByUuid(staff.getUniqueId());
                if (!profile.isRequests()) {
                    break;
                }
                i++;
                if (staff.getServer().getInfo().getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                    staff.sendMessage(yasuo.getLangConfig().getString("REQUEST_FORMAT").replace(yasuo.getLangConfig().getString("REQUEST_FORMAT").split("~")[0], "").replace("~", "").replace("%REASON%", request).replace("%PLAYER%", player.getName()));
                } else {
                    staff.sendMessage(yasuo.getLangConfig().getString("REQUEST_FORMAT").replace("%SERVER", player.getServer().getInfo().getName()).replace("~", "").replace("%REASON%", request).replace("%PLAYER%", player.getName()).replace("~", ""));
                }
            }
        }
        player.sendMessage(yasuo.getLangConfig().getString("REQUEST_RECEIVED").replace("%AMOUNT%", String.valueOf(i)));
        yasuo.getProxy().getScheduler().schedule(yasuo, new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    player.sendMessage(yasuo.getLangConfig().getString("REQUEST_AGAIN"));
                }
            }
        }, yasuo.getMainConfig().getInt("REQUEST_COOLDOWN"), TimeUnit.SECONDS);
    }
}