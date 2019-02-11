package org.hcgames.icefyre.party;

import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.util.ItemBuilder;
import org.hcgames.icefyre.util.file.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PartyHandler {

    private Icefyre main;
    private ConfigFile configFile;
    private ConfigFile langFile;

    public PartyHandler(Icefyre main) {
        this.main = main;
        this.configFile = main.getConfigFile();
        this.langFile = main.getLangFile();
    }

    public void setupProfile(Profile profile) {
        Player player = profile.getPlayer();
        player.getInventory().clear();

        //TODO: Clean this shit up. again.
        Inventory inventory = player.getInventory();
        inventory.setItem(0, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.FIGHT.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.FIGHT.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.TEAM.LEADER.FIGHT.LORE")).build());
        inventory.setItem(1, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.DELETE.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.DELETE.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.TEAM.LEADER.DELETE.LORE")).build());
        inventory.setItem(8, new ItemBuilder(Material.valueOf(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.LIST.ITEM"))).name(configFile.getString("HOTBAR_ITEMS.TEAM.LEADER.LIST.NAME")).lore(configFile.getStringList("HOTBAR_ITEMS.TEAM.LEADER.LIST.LORE")).build());

        player.updateInventory();
        player.getInventory().setHeldItemSlot(0);
    }

    public void sendTeamPlayerListMessage(Player player, Party party) {
        List<String> toSend = new ArrayList<>();

        for (String string : langFile.getStringList("PARTY.LIST_MEMBERS")) {
            if (string.contains("%MEMBERS%")) {
                String prefix = string.replace("%MEMBERS%", ""); //?
                toSend.add(prefix + party.getLeader().getName());
                for (Profile member : party.getMembers()) {
                    toSend.add(prefix + member.getName());
                }
            } else {
                toSend.add(string);
            }
        }

        for (String message : toSend) {
            player.sendMessage(message);
        }
    }
}
