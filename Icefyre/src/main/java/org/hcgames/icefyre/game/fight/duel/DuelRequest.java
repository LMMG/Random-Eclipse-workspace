package org.hcgames.icefyre.game.fight.duel;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class DuelRequest {

    private static Icefyre main = Icefyre.getInstance();

    @Getter @Setter private Ladder ladder;
    @Getter @Setter private Arena arena;
    @Getter @Setter private DuelRequestStage stage = DuelRequestStage.SELECTING_LADDER;
    @Getter @Setter private boolean party;
    @Getter private long init = System.currentTimeMillis();

    public DuelRequest(boolean party) {
        this.party = party;
    }

    public static Inventory getLadderInventory() {
        int size = (int) (9*(Math.ceil(Math.abs(Queue.getQueues(QueueType.UNRANKED).size()/9))));

        if (size <= 9) {
            size = 9;
        }

        Inventory toReturn = Bukkit.createInventory(null, size, main.getLangFile().getString("DUEL.INVENTORY.LADDER_TITLE"));

        for (Queue queue : Queue.getQueues(QueueType.UNRANKED)) {
            toReturn.addItem(new ItemBuilder(queue.getLadder().getIcon().clone()).lore(new ArrayList<>()).build());
        }

        return toReturn;
    }

    public static Inventory getArenaInventory(Ladder ladder) {
        int size = (int) (9*(Math.ceil(Math.abs(ladder.getArenas().size()/9))));

        if (size <= 9) {
            size = 9;
        }

        Inventory toReturn = Bukkit.createInventory(null, size, main.getLangFile().getString("DUEL.INVENTORY.ARENA_TITLE").replace("%LADDER%", ladder.getName()));

        for (Arena arena : ladder.getArenas()) {
            toReturn.addItem(arena.getIcon());
        }

        return toReturn;
    }


}
