package org.hcgames.icefyre.game.kit;

import lombok.Getter;
import lombok.Setter;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {

    @Getter @Setter private String name;
    @Getter private Ladder ladder;
    @Getter @Setter private List<ItemStack> contents, armor;

    public Kit(String name, Ladder ladder, List<ItemStack> contents, List<ItemStack> armor) {
        this.name = name;
        this.ladder = ladder;
        this.contents = contents;
        this.armor = armor;
    }

    public void apply(Player player) {
        player.getInventory().setContents(contents.toArray(new ItemStack[contents.size()]));
        player.getInventory().setArmorContents(armor.toArray(new ItemStack[armor.size()]));
    }
}
