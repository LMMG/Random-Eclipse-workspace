package org.hcgames.icefyre.game.ladder;

import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.arena.Arena;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.util.InventorySerialisation;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LadderHandler {

    private Icefyre main;
    private ConfigFile ladderFile;

    public LadderHandler(Icefyre main) {
        this.main = main;
        this.ladderFile = main.getLadderFile();

        setup();
    }

    public void save() {
        boolean save = false;

        for (String key : ladderFile.getConfiguration().getKeys(false)) {
            String name = ladderFile.getString(key + ".NAME");
            Ladder ladder = Ladder.getByName(name);
            if (ladder != null) {
                if (ladder.getDefaultKit() != null) {
                    save = true;
                    ladderFile.getConfiguration().set(key + ".KIT.ARMOR", InventorySerialisation.itemStackArrayToBase64(ladder.getDefaultKit().getArmor().toArray(new ItemStack[ladder.getDefaultKit().getArmor().size()])));
                    ladderFile.getConfiguration().set(key + ".KIT.CONTENTS", InventorySerialisation.itemStackArrayToBase64(ladder.getDefaultKit().getContents().toArray(new ItemStack[ladder.getDefaultKit().getContents().size()])));
                }
            }
        }

        if (save) {
            try {
                ladderFile.getConfiguration().save(ladderFile.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setup() {
        for (String key : ladderFile.getConfiguration().getKeys(false)) {

            String name = ladderFile.getString(key + ".NAME");
            ItemStack icon = new ItemBuilder(Material.valueOf(ladderFile.getString(key + ".ICON.ITEM")))
                    .name(ladderFile.getString(key + ".ICON.NAME"))
                    .lore(ladderFile.getStringList(key + ".ICON.LORE"))
                    .durability(ladderFile.getInt(key + ".ICON.DATA"))
                    .build();

            Ladder ladder = new Ladder(name, icon, ladderFile.getBoolean(key + ".RANKED"), ladderFile.getBoolean(key + ".UNRANKED"), ladderFile.getInt(key + ".DEFAULT_RANKING"));

            if (ladder.isRanked()) {
                new Queue(ladder, QueueType.RANKED);
            }

            if (ladder.isUnranked()) {
                new Queue(ladder, QueueType.UNRANKED);
            }

            for (String arenaName : ladderFile.getStringList(key + ".ARENAS")) {
                System.out.println("ARENA: " + arenaName);
                Arena arena = Arena.getByName(arenaName);
                if (arena != null) {
                    ladder.getArenas().add(arena);
                    System.out.println("Added arena named " + arena.getName() + " to " + ladder.getName() + ".");
                }
            }

            if (ladderFile.getConfiguration().contains(key + ".KIT")) {
                try {
                    List<ItemStack> armor = new ArrayList<>();
                    List<ItemStack> contents = new ArrayList<>();

                    armor.addAll(Arrays.asList(InventorySerialisation.itemStackArrayFromBase64(ladderFile.getString(key + ".KIT.ARMOR"))));
                    contents.addAll(Arrays.asList(InventorySerialisation.itemStackArrayFromBase64(ladderFile.getString(key + ".KIT.CONTENTS"))));

                    ladder.setDefaultKit(new Kit("Default " + ladder.getName() + " Kit", ladder, contents, armor));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Loaded ladder '" + ladder.getName() + "'!");

            if (!(ladder.isRanked()) && !(ladder.isUnranked())) {
                System.out.println("The ladder named '" + ladder.getName() + "' does not have any queue!");
            }
        }
    }

}
