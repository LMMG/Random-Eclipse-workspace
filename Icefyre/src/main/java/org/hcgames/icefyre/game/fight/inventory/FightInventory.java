package org.hcgames.icefyre.game.fight.inventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.ItemBuilder;

import java.util.Collections;
import java.util.List;

public class FightInventory {

    private static Icefyre main = Icefyre.getInstance();

    private final Profile profile;
    @Getter private final List<ItemStack> armor, contents;
    @Getter private final int health;

    public FightInventory(Profile profile, List<ItemStack> contents, List<ItemStack> armor, int health) {
        this.profile = profile;
        this.contents = contents;
        this.health = health;

        Collections.reverse(armor);

        this.armor = armor;
    }

    public Inventory get() {
        Inventory toReturn = Bukkit.createInventory(null, 9 * 5, main.getLangFile().getString("FIGHT.INVENTORY.TITLE").replace("%PLAYER%", profile.getName()));

        for (int i = 0; i < contents.size(); i++) {
            if (i <= 8) {
                ItemStack item = contents.get(i);
                if (item != null) {
                    toReturn.setItem(i, item);
                }
            }
        }

        for (int i = 0; i < contents.size(); i++) {
            if (i > 8) {
                ItemStack item = contents.get(i);
                if (item != null) {
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

        ItemStack health = new ItemBuilder(Material.valueOf(main.getLangFile().getString("FIGHT.INVENTORY.HEALTH.ITEM")))
                .name(main.getLangFile().getString("FIGHT.INVENTORY.HEALTH.NAME").replace("%HEALTH%", this.health / 2.0 + ""))
                .durability(main.getLangFile().getInt("FIGHT.INVENTORY.HEALTH.DATA"))
                .build();

        ItemStack potions = new ItemBuilder(Material.valueOf(main.getLangFile().getString("FIGHT.INVENTORY.POTIONS.ITEM")))
                .name(main.getLangFile().getString("FIGHT.INVENTORY.POTIONS.NAME").replace("%AMOUNT%", getHealthPotionsAmount() + ""))
                .amount(getHealthPotionsAmount())
                .durability(main.getLangFile().getInt("FIGHT.INVENTORY.POTIONS.DATA"))
                .build();

        for (int i = 0; i < 3; i++) {
            toReturn.setItem(40 + i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name(" ").build());
        }

        for (int i = 0; i < armor.size(); i++) {
            ItemStack item = armor.get(i);
            System.out.println(item);
            if (item != null && item.getType() != Material.AIR) {
                toReturn.setItem(36 + i, item);
            } else {
                toReturn.setItem(36 + i, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).name(" ").build());
            }
        }

        toReturn.setItem(43, health);
        toReturn.setItem(44, potions);

        return toReturn;
    }

    private int getHealthPotionsAmount() {
        int toReturn = 0;

        for (ItemStack itemStack : contents) {
            if (itemStack != null && itemStack.getType() == Material.POTION && itemStack.getDurability() == 16421) {
                toReturn++;
            }
        }

        return toReturn;
    }
}
