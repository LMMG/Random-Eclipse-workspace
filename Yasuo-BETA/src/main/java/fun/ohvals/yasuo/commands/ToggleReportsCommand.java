package fun.ohvals.yasuo.commands;

import fun.ohvals.yasuo.Yasuo;
import fun.ohvals.yasuo.profile.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Yasuo was created by dylan on 06/2017.
 * Please do not redistribute without permission of the developer.
 */

public class ToggleReportsCommand extends Command {

    private Yasuo yasuo = Yasuo.getYasuo();

    public ToggleReportsCommand() {
        super("togglereports", "yasuo.report.toggle", "treports", "reports");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        if (!sender.hasPermission("yasuo.report.toggle")) {
            sender.sendMessage(yasuo.getLangConfig().getConfig().getString("NO_PERMISSION"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        Profile profile = yasuo.getProfileManager().getByUuid(player.getUniqueId());

        profile.setReports(!profile.isReports());
        player.sendMessage(((profile.isReports()) ? yasuo.getLangConfig().getString("REPORT_TOGGLED_ON"):yasuo.getLangConfig().getString("REPORT_TOGGLED_OFF")));
    }

}
