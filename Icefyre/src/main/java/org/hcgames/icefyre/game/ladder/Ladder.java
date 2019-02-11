package org.hcgames.icefyre.game.ladder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.arena.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ladder {

    private static List<Ladder> ladders = new ArrayList<>();
    private static Icefyre main = Icefyre.getInstance();

    @Getter private String name;
    @Getter @Setter private Kit defaultKit;
    @Getter private ItemStack icon;
    @Getter private List<Arena> arenas;
    @Getter private boolean ranked, unranked;
    @Getter private int defaultRanking;

    public Ladder(String name, ItemStack icon, boolean ranked, boolean unranked, int defaultRanking) {
        this.name = name;
        this.icon = icon;
        this.arenas = new ArrayList<>();
        this.ranked = ranked;
        this.unranked = unranked;
        this.defaultRanking = defaultRanking;

        ladders.add(this);
    }

    public Inventory getKitBuilderInventory() {
        if (defaultKit == null) return null;

        Inventory toReturn = Bukkit.createInventory(null, 9 * 4, main.getLangFile().getString("KIT_BUILDER.BUILDING_INVENTORY").replace("%LADDER%", name));

        List<ItemStack> armor = new ArrayList<>(defaultKit.getArmor());
        Collections.reverse(armor);
        for (int i = 0; i < armor.size(); i++) {
            toReturn.setItem(9 * i, armor.get(i));
        }

        for (int i = 0; i < 8; i++) {
            if (i + 1 < defaultKit.getContents().size()) {
                if (i == 7) {
                    i = 8;
                }
                ItemStack item = defaultKit.getContents().get(i);
                if (item != null) {

                    int position = i;
                    while (toReturn.getItem(position) != null) {
                        position++;
                        if (position == toReturn.getSize()) break;
                    }

                    if (position != toReturn.getSize()) {
                        System.out.println(position + ", " + item.getType().name());
                        toReturn.setItem(position, item);
                    }
                }
            }
        }

        int yPos = 2;
        for (int i = 0; i < defaultKit.getContents().size(); i++) {
            if (i > 8) {
                ItemStack item = defaultKit.getContents().get(i);
                if (item != null) {

                    if (i % 9 == 8) {
                        toReturn.setItem(9 * yPos - 1, item);
                        yPos++;
                        continue;
                    }

                    int position = i;

                    if (position <= 17) {
                        position += 18;
                    } else if (position >= 27) {
                        position -= 18;
                    }

                    while (toReturn.getItem(position) != null) {
                        position++;
                        if (position == toReturn.getSize()) break;
                    }

                    if (position != toReturn.getSize()) {
                        toReturn.setItem(position, item);
                    }
                }
            }
        }

        return toReturn;
    }

    public static Ladder getByName(String name) {
        for (Ladder ladder : ladders) {
            if (ladder.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))) {
                return ladder;
            }
        }
        return null;
    }

    public Arena getRandomArena() {
        return arenas.isEmpty() ? null : arenas.get(new Random().nextInt(arenas.size()));
    }

    public static List<Ladder> getLadders() {
        return ladders;
    }
}
