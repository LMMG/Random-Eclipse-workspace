package org.ipvp.hcf.faction.argument.staff;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.faction.type.Faction;
import org.ipvp.hcf.faction.type.PlayerFaction;

import com.doctordark.util.command.CommandArgument;
import com.google.common.base.Joiner;

public class FactionMuteArgument extends CommandArgument
{
	
	public static final Joiner SPACE_JOINER;
	
	static {
		 SPACE_JOINER = Joiner.on(' ');
	}
	
    private final HCF plugin;
    
    public FactionMuteArgument(final HCF plugin) {
        super("mute", "Mutes every member in this faction.");
        this.plugin = plugin;
        this.permission = "hcf.command.faction.argument." + this.getName();
    }
    
    @Override
    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName() + " <factionName> <time:(e.g. 1h2s)> <reason>";
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        final Faction faction = this.plugin.getFactionManager().getContainingFaction(args[1]);
        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return true;
        }
        final PlayerFaction playerFaction = (PlayerFaction)faction;
        final String extraArgs = SPACE_JOINER.join((Object[])Arrays.copyOfRange(args, 2, args.length));
        final ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (final UUID uuid : playerFaction.getMembers().keySet()) {
            final String commandLine = "tempmute " + uuid.toString() + " " + extraArgs;
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Executing " + ChatColor.RED + commandLine);
            console.getServer().dispatchCommand(sender, commandLine);
        }
        sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Executed mute action on faction " + playerFaction.getName() + ".");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 2) ? null : Collections.emptyList();
    }
}
