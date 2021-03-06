package com.prevailpots.kitmap.faction.argument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.customhcf.util.command.CommandArgument;
import com.google.common.base.Enums;
import com.prevailpots.kitmap.HCF;
import com.prevailpots.kitmap.faction.LandMap;
import com.prevailpots.kitmap.user.FactionUser;
import com.prevailpots.kitmap.visualise.VisualType;

public class FactionMapArgument extends CommandArgument {
    private final HCF plugin;

    public FactionMapArgument(final HCF plugin) {
        super("map", "View claims around your current location.");
        this.plugin = plugin;
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " [factionName]";
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        final FactionUser factionUser = this.plugin.getUserManager().getUser(uuid);
        VisualType visualType;
        if(args.length <= 1) {
            visualType = VisualType.CLAIM_MAP;
        } else if((visualType = (VisualType) Enums.getIfPresent((Class) VisualType.class, args[1]).orNull()) == null) {
            player.sendMessage(ChatColor.RED + "Visual type " + args[1] + " not found.");
            return true;
        }
        final boolean newShowingMap = !factionUser.isShowClaimMap();
        if(newShowingMap) {
            if(!LandMap.updateMap(player, this.plugin, visualType, true)) {
                return true;
            }
        } else {
            this.plugin.getVisualiseHandler().clearVisualBlocks(player, visualType, null);
            sender.sendMessage(ChatColor.RED + "Faction land claim pillars are no longer shown.");
        }
        factionUser.setShowClaimMap(newShowingMap);
        return true;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        final VisualType[] values = VisualType.values();
        final List<String> results = new ArrayList<String>(values.length);
        for(final VisualType visualType : values) {
            results.add(visualType.name());
        }
        return results;
    }
}
