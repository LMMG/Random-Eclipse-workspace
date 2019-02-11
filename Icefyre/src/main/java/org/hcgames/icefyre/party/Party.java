package org.hcgames.icefyre.party;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.profile.Profile;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.hcgames.icefyre.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private static Icefyre main = Icefyre.getInstance();
    private static List<Party> parties = new ArrayList<>();

    @Getter @Setter private Profile leader;
    @Getter private List<Profile> members;

    public Party(Profile leader) {
        this.leader = leader;
        this.members = new ArrayList<>();

        parties.add(this);
    }

    public boolean contains(Profile profile) {
        return members.contains(profile) || isLeader(profile.getPlayer());
    }

    public boolean isLeader(Player player) {
        return player.getUniqueId().equals(leader.getPlayer().getUniqueId());
    }

    public void sendMessage(String message) {
        leader.getPlayer().sendMessage(message);
        for (Profile profile : members) {
            profile.getPlayer().sendMessage(message);
        }
    }

    public static Party getByPlayer(Player player) {
        Profile profile = Profile.getByPlayer(player);
        if (profile != null) {
            for (Party party : parties) {
                if (party.getLeader().equals(profile) || party.getMembers().contains(profile)) {
                    return party;
                }
            }
        }
        return null;
    }

    private static Inventory populatePartyInventory(Inventory inventory, int page) {
        int total = (int) Math.ceil(getParties().size() / 36.0);

        inventory.setItem(0, new ItemBuilder(Material.valueOf(main.getLangFile().getString("PARTY.INVENTORY.PREVIOUS_PAGE.ITEM"))).durability(main.getLangFile().getInt("PARTY.INVENTORY.PREVIOUS_PAGE.DATA")).name(main.getLangFile().getString("PARTY.INVENTORY.PREVIOUS_PAGE.NAME")).build());
        inventory.setItem(8, new ItemBuilder(Material.valueOf(main.getLangFile().getString("PARTY.INVENTORY.NEXT_PAGE.ITEM"))).durability(main.getLangFile().getInt("PARTY.INVENTORY.NEXT_PAGE.DATA")).name(main.getLangFile().getString("PARTY.INVENTORY.NEXT_PAGE.NAME")).build());
        inventory.setItem(4, new ItemBuilder(Material.valueOf(main.getLangFile().getString("PARTY.INVENTORY.INFO.ITEM"))).durability(main.getLangFile().getInt("PARTY.INVENTORY.INFO.DATA")).name(main.getLangFile().getString("PARTY.INVENTORY.INFO.NAME").replace("%TOTAL%", (total == 0 ? 1 : total) + "").replace("%PAGE%", page + "")).build());

        int pos = 0;
        int itemPos = 0;
        page-=1;

        for (Party party : parties) {
            if (pos < (36 * page + 36) && pos >= (36 * page)) {
                List<String> lore = new ArrayList<>();

                for (Profile profile : party.getMembers()) {
                    lore.add(main.getLangFile().getString("PARTY.INVENTORY.PARTY.MEMBER") + profile.getName());
                }

                ItemStack item = new ItemBuilder(Material.SKULL_ITEM).durability(3).owner(party.getLeader().getName())
                        .name(main.getLangFile().getString("PARTY.INVENTORY.PARTY.NAME").replace("%LEADER%", party.getLeader().getName()))
                        .lore(lore)
                        .build();

                if (party.getLeader().getPartyFight() != null) {
                    item = new ItemBuilder(item).type(Material.BEDROCK).build();
                }

                inventory.setItem(9 + itemPos, item);
                itemPos++;
            }
            pos++;
        }

        for (int i = 9 + itemPos; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }

        return inventory;
    }

    public static Inventory getPartyInventory(int page) {
        int total = (int) Math.ceil(getParties().size() / 36.0);
        return populatePartyInventory(Bukkit.createInventory(null, 9 * 5, Icefyre.getInstance().getLangFile().getString("PARTY.INVENTORY.TITLE").replace("%PAGE%", page + "").replace("%TOTAL%", (total == 0 ? 1 : total) + "")), page);
    }

    public static void run(Icefyre main) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    String title = inventory.getTitle();
                    if (title.contains(main.getLangFile().getString("PARTY.INVENTORY.TITLE").split(" ")[0])) {
                        int page = Integer.parseInt(title.substring(title.lastIndexOf("/") - 1, title.lastIndexOf("/")));
                        populatePartyInventory(inventory, page);
                    }
                }
            }
        }.runTaskTimerAsynchronously(main, 2L, 2L);
    }


    public static List<Party> getParties() {
        return parties;
    }
}
