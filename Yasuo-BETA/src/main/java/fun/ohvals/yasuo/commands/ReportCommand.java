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

public class ReportCommand extends Command {

    private Yasuo yasuo = Yasuo.getYasuo();

    private Map<UUID, Long> cd;

    public ReportCommand() {
        super("report", "yasuo.report", "hacker");
        cd = new HashMap<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        if (!sender.hasPermission("yasuo.report")) {
            sender.sendMessage(yasuo.getLangConfig().getConfig().getString("NO_PERMISSION"));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length < 2) {
            player.sendMessage(yasuo.getLangConfig().getString("USAGE") + "report <player> <reason...>");
            return;
        }

        if (cd.containsKey(player.getUniqueId()) && System.currentTimeMillis()/1000 - cd.get(player.getUniqueId()) <= yasuo.getMainConfig().getInt("REPORT_COOLDOWN")) {
            player.sendMessage(yasuo.getLangConfig().getString("REPORT_COOLDOWN"));
            return;
        }

        ProxiedPlayer reported = yasuo.getProxy().getPlayer(args[0]);

        if (!reported.getServer().getInfo().getName().equalsIgnoreCase(player.getServer().getInfo().getName()) || reported == null) {
            player.sendMessage(yasuo.getLangConfig().getString("NOT_ONLINE").replace("%REPORTED%", args[0]));
            return;
        }

        cd.put(player.getUniqueId(), System.currentTimeMillis()/1000);

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            if (!args[i].isEmpty()) {
                sb.append(args[i]);
                if (i + 1 < args.length) {
                    sb.append(" ");
                }
            }
        }

        String report = sb.toString();

        int i = 0;
        for (ProxiedPlayer staff : yasuo.getProxy().getPlayers()) {
            if (staff.hasPermission("yasuo.report.receive")) {
                Profile profile = yasuo.getProfileManager().getByUuid(staff.getUniqueId());
                if (!profile.isRequests()) {
                    break;
                }
                i++;
                if (staff.getServer().getInfo().getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
                    staff.sendMessage(yasuo.getLangConfig().getString("REPORT_FORMAT").replace(yasuo.getLangConfig().getString("REPORT_FORMAT").split("~")[0], "").replace("~", "").replace("%PLAYER%", player.getName()).replace("%REPORTED%", reported.getName()).replace("%REASON%", report));
                } else {
                    staff.sendMessage(yasuo.getLangConfig().getString("REPORT_FORMAT").replace("%SERVER%", player.getServer().getInfo().getName()).replace("~", "").replace("%PLAYER%", player.getName()).replace("%REPORTED%", reported.getName()).replace("%REASON%", report));
                }
            }
        }
        player.sendMessage(yasuo.getLangConfig().getString("REPORT_RECEIVED").replace("%AMOUNT%", String.valueOf(i)));
        yasuo.getProxy().getScheduler().schedule(yasuo, new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    player.sendMessage(yasuo.getLangConfig().getString("REPORT_AGAIN"));
                }
            }
        }, yasuo.getMainConfig().getInt("REPORT_COOLDOWN"), TimeUnit.SECONDS);
    }
}
