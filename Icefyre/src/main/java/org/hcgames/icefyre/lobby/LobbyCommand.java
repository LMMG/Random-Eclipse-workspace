package org.hcgames.icefyre.lobby;

import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;
import org.hcgames.icefyre.util.command.BaseCommand;
import org.hcgames.icefyre.util.command.Command;
import org.hcgames.icefyre.util.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LobbyCommand extends BaseCommand {

    @Override
    @Command(name = "lobby", aliases = {"practice", "icefyre"}, isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/" + command.getLabel() + " <setspawn/setbuilder>");
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setspawn")) {
                main.getLobby().setLocation(player.getLocation());
                player.sendMessage(ChatColor.RED + "You have updated the lobby location.");
                return;
            }

            if (args[0].equals("setbuilder") || args[0].equalsIgnoreCase("setkitbuilder")) {
                main.getLobby().getKitBuilder().setLocation(player.getLocation());
                player.sendMessage(ChatColor.RED + "You have updated the kit builder location.");
                return;
            }

            if (args[0].equals("leave")) {
                Profile profile = Profile.getByPlayer(player);
                if (profile != null) {

                    if (profile.getState() != ProfileState.IN_LOBBY) {
                        player.sendMessage(ChatColor.RED + "You're not in the lobby state!");
                        return;
                    }

                    profile.setState(ProfileState.IN_STAFF);
                    player.sendMessage(ChatColor.RED + "You have left the lobby state.");
                    player.getInventory().clear();
                    return;
                }
            }

            if (args[0].equalsIgnoreCase("join")) {
                Profile profile = Profile.getByPlayer(player);
                if (profile != null) {

                    if (profile.getState() == ProfileState.IN_LOBBY) {
                        player.sendMessage(ChatColor.RED + "You're already in the lobby state!");
                        return;
                    }

                    profile.setState(ProfileState.IN_LOBBY);
                    player.sendMessage(ChatColor.RED + "You have joined the lobby state.");
                    main.getLobby().teleportToSpawn(player);
                    main.getLobby().setupPlayer(player);
                }
            }
        }
    }
}
