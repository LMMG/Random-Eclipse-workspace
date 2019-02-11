package net.nersa.player;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class KillReward {

	private ArrayList<ItemStack> items;

	public KillReward(ItemStack... items) {
		if(items == null) return;

		this.items = new ArrayList<>(Arrays.asList(items));
	}

	public ItemStack[] getItems() {
		return items.toArray(new ItemStack[0]);
	}

	public void addItem(ItemStack item) {
		items.add(item);
	}

}