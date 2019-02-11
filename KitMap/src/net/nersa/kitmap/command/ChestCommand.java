package net.nersa.kitmap.command;

import net.nersa.kitmap.HCF;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ChestCommand implements CommandExecutor, Listener {

	public static HashMap<String, Inventory> chests = new HashMap<String, Inventory>();
	private HCF plugin;
	File chestConfig;
	FileConfiguration config;

	public ChestCommand(HCF plugin) {
		this.plugin = plugin;
		chestConfig = new File(plugin.getDataFolder() + "/playerVaults.yml");
		config = YamlConfiguration.loadConfiguration(chestConfig);

		Bukkit.getPluginManager().registerEvents(this, HCF.getInstance());
	}

	public static Inventory generateInventory(Player player) {
		int rows = 9;

		if(player.hasPermission("vip.chest")) {
			rows = 27;
		}

		if(player.hasPermission("mvp.chest")) {
			rows = 45;
		}

		if(player.hasPermission("legend.chest")) {
			rows = 63;
		}

		Inventory chest = Bukkit.createInventory(null, rows, ChatColor.WHITE + player.getName() + "'s vault");

		if (chests.containsKey(player.getUniqueId().toString())) {
			chest.setContents(chests.get(player.getUniqueId().toString()).getContents());
		}

		return chest;
	}

	public String toBase64(Inventory inventory) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			dataOutput.writeInt(inventory.getSize());

			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}

			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save items.", e);
		}
	}

	public Inventory fromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}
			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to find items.", e);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (chests.containsKey(e.getPlayer().getUniqueId().toString())) {
			chests.remove(e.getPlayer().getUniqueId().toString());
		}
		if (config.contains("Chests." + e.getPlayer().getUniqueId().toString())) {

			try {
				chests.put(e.getPlayer().getUniqueId().toString(), fromBase64(config.getString("chests." + e.getPlayer().getUniqueId().toString())));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (chests.containsKey(e.getPlayer().getUniqueId().toString())) {
			config.set("Chests." + e.getPlayer().getUniqueId().toString(), toBase64(chests.get(e.getPlayer().getUniqueId().toString())));
			try {
				config.save(chestConfig);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chests.remove(e.getPlayer().getUniqueId().toString());
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (chests.containsKey(e.getPlayer().getUniqueId().toString())) {
			config.set("Chests." + e.getPlayer().getUniqueId().toString(), toBase64(chests.get(e.getPlayer().getUniqueId().toString())));
			try {
				config.save(chestConfig);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chests.remove(e.getPlayer().getUniqueId().toString());
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {

		if (e.getInventory() == null) {
			return;
		}
		if (e.getPlayer() == null) {
			return;
		}
		if (e.getInventory().getTitle() == null) {
			return;
		}
		if (e.getInventory().getTitle().toLowerCase().contains(e.getPlayer().getName().toLowerCase())) {
			if (chests.containsKey(e.getPlayer().getUniqueId().toString())) {
				chests.remove(e.getPlayer().getUniqueId().toString());
			}
			chests.put(e.getPlayer().getUniqueId().toString(), e.getInventory());
		} else {
			return;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
			return true;
		}
		Player player = (Player) sender;

		if (plugin.getTimerManager().getCombatTimer().getRemaining(player) != 0) {
			player.sendMessage(ChatColor.RED + "You may not use this command whilst combat tagged!");
			return true;
		}

		int rows = 9;

		if(player.hasPermission("vip.chest")) {
			rows = 27;
		}

		if(player.hasPermission("mvp.chest")) {
			rows = 45;
		}

		if(player.hasPermission("legend.chest")) {
			rows = 63;
		}

		Inventory chest = Bukkit.createInventory(null, rows, ChatColor.WHITE + player.getName() + "'s vault");

		if (chests.containsKey(player.getUniqueId().toString())) {
			chest.setContents(chests.get(player.getUniqueId().toString()).getContents());
		} else {
		}

		player.openInventory(chest);
		return true;
	}

}