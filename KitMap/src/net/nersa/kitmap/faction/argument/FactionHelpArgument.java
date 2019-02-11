package net.nersa.kitmap.faction.argument;

import com.doctordark.util.BukkitUtils;
import com.doctordark.util.command.CommandArgument;

import net.minecraft.util.com.google.common.collect.ArrayListMultimap;
import net.minecraft.util.com.google.common.collect.ImmutableMultimap;
import net.minecraft.util.com.google.common.collect.Multimap;
import net.minecraft.util.com.google.common.primitives.Ints;
import net.nersa.kitmap.faction.FactionExecutor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionHelpArgument extends CommandArgument {
	private static final int HELP_PER_PAGE = 10;
	private final FactionExecutor executor;
	private ImmutableMultimap<Integer, String> pages;

	public FactionHelpArgument(FactionExecutor executor) {
		super("help", "View help on how to use factions.");
		this.executor = executor;
	}

	public String getUsage(String label) {
		return "" + '/' + label + ' ' + this.getName();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 2) {
			this.showPage(sender, label, 1);
			return true;
		}
		Integer page = Ints.tryParse((String) args[1]);
		if (page == null) {
			sender.sendMessage(ChatColor.RED + "That number is invalid/minus.");
			return true;
		}
		this.showPage(sender, label, page);
		return true;
	}

	private void showPage(CommandSender sender, String label, int pageNumber) {
		if (this.pages == null) {
			boolean isPlayer = sender instanceof Player;
			int val = 1;
			int count = 0;
			ArrayListMultimap pages = ArrayListMultimap.create();
			for (com.doctordark.util.command.CommandArgument argument : this.executor.getArguments()) {
				String permission;
				if (argument.equals((Object) this) || (permission = argument.getPermission()) != null && !sender.hasPermission(permission) || argument.isPlayerOnly() && !isPlayer)
					continue;
				pages.get((Object) val).add((Object) ChatColor.YELLOW + "/" + label + ' ' + argument.getName() + (Object) ChatColor.GRAY + " - " + (Object) ChatColor.WHITE + argument.getDescription());
				if (++count % 10 != 0) continue;
				++val;
			}
			this.pages = ImmutableMultimap.copyOf((Multimap) pages);
		}
		int totalPageCount = this.pages.size() / 10 + 1;
		int maxPages = pages.size();
		if (pageNumber < 1) {
			sender.sendMessage((Object) ChatColor.RED + "You cannot view a page less than 1.");
			return;
		}
		if (pageNumber > totalPageCount) {
			sender.sendMessage((Object) ChatColor.RED + "There are only " + totalPageCount + " pages.");
			return;
		}
		sender.sendMessage((Object) ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage((Object) ChatColor.GOLD + "Faction Related Help" + (Object) ChatColor.WHITE + " (Page " + pageNumber + '/' + totalPageCount + ')');
		sender.sendMessage("");
		for (String message : this.pages.get(pageNumber)) {
			sender.sendMessage("  " + message);
		}
		sender.sendMessage(ChatColor.WHITE + " You are currently on " + ChatColor.GOLD + "Page " + pageNumber + '/' + 5 + ChatColor.WHITE + '.');
		sender.sendMessage(ChatColor.WHITE + " To view other pages, use " + ChatColor.GOLD + '/' + label + ' ' + getName() + " <page#>" + ChatColor.WHITE + '.');
		sender.sendMessage((Object) ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
	}
}