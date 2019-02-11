package rip.cobalt.commands.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import rip.cobalt.listener.StaffModeListener;

public class Staffmode implements CommandExecutor
{

	StaffModeListener listener;
	public ArrayList<UUID> staffmode = new ArrayList<UUID>();
	public HashMap<UUID, ItemStack[]> inv = new HashMap<UUID, ItemStack[]>();
	public HashMap<UUID, ItemStack[]> armor = new HashMap<UUID, ItemStack[]>();

	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
	{
		if (!(cs instanceof Player)) { return true; }
		Player p = (Player) cs;
		if (cmd.getName().equalsIgnoreCase("mod"))
		{
			if (p.hasPermission("core.staff"))
			{
				if (args.length == 0)
				{
					inv.put(p.getUniqueId(), p.getInventory().getContents());
					armor.put(p.getUniqueId(), p.getInventory().getArmorContents());
					p.getInventory().clear();
					listener.GiveCompass(p, 0);
					listener.GiveFreeze(p, 3);
					listener.GiveWorldedit(p, 6);
					listener.GiveFeather(p, 9);
					staffmode.add(p.getUniqueId());
					p.sendMessage(ChatColor.GREEN + "Your staffMode has been set to true!");
					return true;
				}
				else
				{
					staffmode.remove(p.getUniqueId());
					inv.get(p.getUniqueId());
					p.getInventory().clear();
					ItemStack[] array;
					for(int length  = (array = inv.get(p.getUniqueId())).length, j = 0; j < length; ++j) {
						ItemStack i = array[j];
						if(i != null) {
							p.getInventory().addItem(i);
						}
					}
					armor.get(p.getUniqueId());
					p.sendMessage(ChatColor.GREEN + "Your staffmode has been set to false!");
					return true;
				}
			}
			else
			{
				p.sendMessage("no perm");
			}
		}
		return true;
	}

}
