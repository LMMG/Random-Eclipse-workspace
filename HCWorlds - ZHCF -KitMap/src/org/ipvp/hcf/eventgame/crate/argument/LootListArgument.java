package org.ipvp.hcf.eventgame.crate.argument;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.ipvp.hcf.HCF;
import org.ipvp.hcf.eventgame.crate.Key;

import com.doctordark.util.command.CommandArgument;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class LootListArgument
extends CommandArgument {
    private final HCF plugin;

    public LootListArgument(HCF plugin) {
        super("list", "List all crate key types");
        this.plugin = plugin;
        this.permission = "hcf.command.loot.argument." + this.getName();
    }

    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List keyNames = this.plugin.getKeyManager().getKeys().stream().map(Key::getDisplayName).collect(Collectors.toList());
        sender.sendMessage((Object)ChatColor.GRAY + "List of key types: " + StringUtils.join(keyNames, (String)new StringBuilder().append((Object)ChatColor.GRAY).append(", ").toString()));
        return true;
    }
}