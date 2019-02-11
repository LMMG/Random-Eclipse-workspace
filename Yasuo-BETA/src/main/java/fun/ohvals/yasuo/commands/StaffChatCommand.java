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

public class StaffChatCommand extends Command {

    private Yasuo yasuo = Yasuo.getYasuo();

    public StaffChatCommand() {
        super("staffchat", "yasuo.staffchat", "sc", "ac", "adminchat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        if (!sender.hasPermission("yasuo.staffchat")) {
            sender.sendMessage(yasuo.getLangConfig().getConfig().getString("NO_PERMISSION"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        Profile profile = yasuo.getProfileManager().getByUuid(player.getUniqueId());

        if (!profile.isInStaffchat()) {
            player.sendMessage(yasuo.getLangConfig().getString("STAFFCHAT_NOT_IN"));
            return;
        }

        if (args.length == 0) {
            profile.setUsingStaffchat(!profile.isUsingStaffchat());
            player.sendMessage(((profile.isUsingStaffchat()) ? yasuo.getLangConfig().getString("STAFFCHAT_NOW_IN"):yasuo.getLangConfig().getString("STAFFCHAT_NOW_OUT")));
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (!args[i].isEmpty()) {
                    sb.append(args[i]).append(" ");
                }
            }
            String message = sb.toString();
            sendStaffMessage(player, message);
        }
    }

    public static void sendStaffMessage(ProxiedPlayer sender, String message) {
        for (ProxiedPlayer player : Yasuo.getYasuo().getProxy().getPlayers()) {
            if (player.hasPermission("yasuo.staffchat.receive")) {
                Profile profile = Yasuo.getYasuo().getProfileManager().getByUuid(player.getUniqueId());
                if (profile.isInStaffchat()) {
                    player.sendMessage(Yasuo.getYasuo().getLangConfig().getString("STAFFCHAT_FORMAT").replace("%SERVER%", (!(sender instanceof ProxiedPlayer) ? "console":((ProxiedPlayer)sender).getServer().getInfo().getName())).replace("%SENDER%", sender.getName()).replace("%MESSAGE%", message));
                }
            }
        }
    }

}
