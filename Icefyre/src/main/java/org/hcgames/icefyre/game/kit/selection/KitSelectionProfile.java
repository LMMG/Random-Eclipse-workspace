package org.hcgames.icefyre.game.kit.selection;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.ladder.Ladder;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.ItemBuilder;

public class KitSelectionProfile {

    private static Icefyre main = Icefyre.getInstance();

    @Getter private final Profile profile;
    @Getter private final Ladder ladder;

    public KitSelectionProfile(Profile profile, Ladder ladder) {
        this.profile = profile;
        this.ladder = ladder;
    }

    public Inventory getInventory() {
        Inventory toReturn = Bukkit.createInventory(null, 9, main.getLangFile().getString("KIT_SELECTOR.TITLE").replace("%LADDER%", ladder.getName()));

        if (ladder.getDefaultKit() != null) {
            toReturn.setItem(8, new ItemBuilder(Material.BOOK).name(main.getLangFile().getString("KIT_SELECTOR.DEFAULT_KIT_ITEM").replace("%LADDER%", ladder.getName())).build());
        }

        for (int i = 0; i < profile.getKits().get(ladder).length; i++) {
            Kit kit = profile.getKits().get(ladder)[i];
            if (kit != null) {
                toReturn.setItem(i, new ItemBuilder(Material.ENCHANTED_BOOK).name(main.getLangFile().getString("KIT_SELECTOR.SAVED_KIT_ITEM").replace("%KIT%", kit.getName())).build());
            }
        }

        return toReturn;
    }
}
