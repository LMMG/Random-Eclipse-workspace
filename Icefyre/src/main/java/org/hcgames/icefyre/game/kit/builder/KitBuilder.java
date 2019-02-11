package org.hcgames.icefyre.game.kit.builder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class KitBuilder {

    private static Icefyre main = Icefyre.getInstance();

    @Getter @Setter private Location location;

    public Inventory getGraphicalInterface(QueueType type) {
        int size = (int) (9*(Math.ceil(Math.abs(Queue.getQueues(type).size()/9))));

        if (size <= 9) {
            size = 9;
        }

        Inventory toReturn = Bukkit.createInventory(null, size, main.getLangFile().getString("KIT_BUILDER.INVENTORY.LADDER_SELECT.TITLE"));

        for (Queue queue : Queue.getQueues(type)) {
            toReturn.addItem(new ItemBuilder(queue.getLadder().getIcon().clone()).lore(new ArrayList<>()).build());
        }

        return toReturn;
    }

}
