package me.kairos.ipunish.commands;

import com.google.common.collect.ImmutableList;
import me.kairos.ipunish.IPunish;
import me.kairos.ipunish.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IPunishCommand implements CommandExecutor, TabCompleter {

    private final IPunish plugin;

    public IPunishCommand(IPunish plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                plugin.getServer().getPluginManager().enablePlugin(plugin);
                sender.sendMessage(ChatColor.GREEN + "You have reloaded " + plugin.getDescription().getFullName() + "!");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "/" + label + " <reload>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Utils.getCompletions(args, new ArrayList<>(COMPLETIONS)) : Collections.emptyList();
    }

    private static final List<String> COMPLETIONS = ImmutableList.of("reload");
}
