package net.badlion.tablist.tab;

import org.bukkit.entity.Player;

import net.badlion.tablist.TabList;
import net.badlion.tablist.TabListMain;
import net.badlion.tablist.TabListManager;

public class Tab extends TabListManager {

	public Tab() {
		super(TabListMain.geTabListMain);
	}
	
	public void createTabList(Player player) {
		TabList tabList = new TabList(player, 1, 60);

		tabList.setPosition(1, "§6§lBadlion SG", false);
	}
	
}
