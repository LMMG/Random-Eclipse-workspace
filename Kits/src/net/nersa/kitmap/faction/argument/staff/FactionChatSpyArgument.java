package net.nersa.kitmap.faction.argument.staff;

import com.doctordark.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;

import net.nersa.kitmap.HCF;
import net.nersa.kitmap.faction.event.FactionChatEvent;
import net.nersa.kitmap.faction.event.FactionRemoveEvent;
import net.nersa.kitmap.faction.type.Faction;
import net.nersa.kitmap.faction.type.PlayerFaction;
import net.nersa.player.PlayerData;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.stream.Collectors;

public class FactionChatSpyArgument extends CommandArgument implements Listener {

	private static final UUID ALL_UUID = UUID.fromString("5a3ed6d1-0239-4e24-b4a9-8cd5b3e5fc72");
	private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("list", "add", "del", "clear");
	private final HCF plugin;

	public FactionChatSpyArgument(HCF plugin) {
		super("chatspy", "Spy on the chat of a faction.");
		this.plugin = plugin;
		this.aliases = new String[]{"cs"};
		this.permission = "hcf.command.faction.argument." + getName();

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String getUsage(String label) {
		return '/' + label + ' ' + getName() + " <" + StringUtils.join(COMPLETIONS, '|') + "> [factionName]";
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionRemove(FactionRemoveEvent event) {
		if (event.getFaction() instanceof PlayerFaction) {
			UUID factionUUID = event.getFaction().getUniqueID();

			for (PlayerData playerData : plugin.getPlayerManager().getAllData().values()) {
				playerData.getFactionChatSpying().remove(factionUUID);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onFactionChat(FactionChatEvent event) {
		Player player = event.getPlayer();
		Faction faction = event.getFaction();
		String format = ChatColor.GOLD + "[" + ChatColor.RED + event.getChatChannel().getDisplayName() + ": " + ChatColor.YELLOW + faction.getName() + ChatColor.GOLD + "] " + ChatColor.GRAY + event.getFactionMember().getRole().getAstrix() + player.getName() + ": " + ChatColor.YELLOW + event.getMessage();

		Collection<CommandSender> recipients = new HashSet<>(Bukkit.getOnlinePlayers());
		recipients.removeAll(event.getRecipients());

		for (CommandSender recipient : recipients) {
			if (!(recipient instanceof Player)) continue;

			Player target = (Player) recipient;
			PlayerData playerData = plugin.getPlayerManager().getPlayerData(target);
			Collection<UUID> spying = playerData.getFactionChatSpying();

			if (spying.contains(ALL_UUID) || spying.contains(faction.getUniqueID())) {
				recipient.sendMessage(format);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
			return true;
		}

		Player player = (Player) sender;
		Set<UUID> currentSpies = plugin.getPlayerManager().getPlayerData(player).getFactionChatSpying();

		if (args[1].equalsIgnoreCase("list")) {
			if (currentSpies.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "You are not spying on the chat of any factions.");
				return true;
			}

			sender.sendMessage(ChatColor.GRAY + "You are currently spying on the chat of (" + currentSpies.size() + " factions): " + ChatColor.RED + StringUtils.join(currentSpies, ChatColor.GRAY + ", " + ChatColor.RED) + ChatColor.GRAY + '.');

			return true;
		}

		if (args[1].equalsIgnoreCase("add")) {
			if (args.length < 3) {
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <all|factionName|playerName>");
				return true;
			}

			Faction faction = plugin.getFactionManager().getFaction(args[2]);

			if (!(faction instanceof PlayerFaction)) {
				sender.sendMessage(ChatColor.RED + "Player based faction named or containing member with IGN or UUID " + args[2] + " not found.");
				return true;
			}

			if (currentSpies.contains(ALL_UUID) || currentSpies.contains(faction.getUniqueID())) {
				sender.sendMessage(ChatColor.RED + "You are already spying on the chat of " + (args[2].equalsIgnoreCase("all") ? "all factions" : args[2]) + '.');
				return true;
			}

			if (args[2].equalsIgnoreCase("all")) {
				currentSpies.clear();
				currentSpies.add(ALL_UUID);
				sender.sendMessage(ChatColor.GREEN + "You are now spying on the chat of all factions.");
				return true;
			}

			if (currentSpies.add(faction.getUniqueID())) {
				sender.sendMessage(ChatColor.GREEN + "You are now spying on the chat of " + faction.getDisplayName(sender) + ChatColor.GREEN + '.');
			} else {
				sender.sendMessage(ChatColor.RED + "You are already spying on the chat of " + faction.getDisplayName(sender) + ChatColor.RED + '.');
			}

			return true;
		}

		if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove")) {
			if (args.length < 3) {
				sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <playerName>");
				return true;
			}

			if (args[2].equalsIgnoreCase("all")) {
				currentSpies.remove(ALL_UUID);
				sender.sendMessage(ChatColor.RED + "No longer spying on the chat of all factions.");
				return true;
			}

			Faction faction = plugin.getFactionManager().getContainingFaction(args[2]);

			if (faction == null) {
				sender.sendMessage(ChatColor.GOLD + "Faction '" + ChatColor.WHITE + args[2] + ChatColor.GOLD + "' not found.");
				return true;
			}

			if (currentSpies.remove(faction.getUniqueID())) {
				sender.sendMessage(ChatColor.RED + "You are no longer spying on the chat of " + faction.getDisplayName(sender) + ChatColor.RED + '.');
			} else {
				sender.sendMessage(ChatColor.RED + "You will still not be spying on the chat of " + faction.getDisplayName(sender) + ChatColor.RED + '.');
			}

			return true;
		}

		if (args[1].equalsIgnoreCase("clear")) {
			currentSpies.clear();
			sender.sendMessage(ChatColor.YELLOW + "You are no longer spying the chat of any faction.");
			return true;
		}

		sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 2) {
			return COMPLETIONS;
		} else if (args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("del"))) {
			if (args[1].isEmpty()) {
				return null;
			}

			List<String> results = new ArrayList<>(plugin.getFactionManager().getFactionNameMap().keySet());
			Player senderPlayer = sender instanceof Player ? ((Player) sender) : null;
			results.addAll(Bukkit.getOnlinePlayers().stream().filter(player -> senderPlayer == null || senderPlayer.canSee(player)).map(Player::getName).collect(Collectors.toList()));
			return results;
		} else {
			return Collections.emptyList();
		}
	}

}