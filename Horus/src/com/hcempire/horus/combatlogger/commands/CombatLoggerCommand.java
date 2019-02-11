package com.hcempire.horus.combatlogger.commands;

import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import com.hcempire.horus.profile.Profile;
import com.hcempire.horus.profile.cooldown.ProfileCooldown;
import com.hcempire.horus.profile.cooldown.ProfileCooldownType;
import com.hcempire.horus.util.HorusCommand;
import org.bukkit.entity.Player;

public class CombatLoggerCommand extends HorusCommand {
    @Command(name = "logout", aliases = "combatlog")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getCooldownByType(ProfileCooldownType.LOGOUT) != null) {
            player.sendMessage(langFile.getString("COMBAT_LOGGER.LOGOUT_ALREADY"));
            return;
        }

        player.sendMessage(langFile.getString("COMBAT_LOGGER.LOGOUT").replace("%TIME%", main.getConfigFile().getInt("COMBAT_LOGGER.LOGOUT_TIME") + ""));
        profile.getCooldowns().add(new ProfileCooldown(ProfileCooldownType.LOGOUT, main.getConfigFile().getInt("COMBAT_LOGGER.LOGOUT_TIME")));
        profile.setLogoutLocation(player.getLocation());
    }
}
