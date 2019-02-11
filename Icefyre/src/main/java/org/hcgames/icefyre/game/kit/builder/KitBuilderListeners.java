package org.hcgames.icefyre.game.kit.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.hcgames.icefyre.Icefyre;
import org.hcgames.icefyre.game.kit.Kit;
import org.hcgames.icefyre.game.queue.Queue;
import org.hcgames.icefyre.game.queue.QueueType;
import org.hcgames.icefyre.party.Party;
import org.hcgames.icefyre.profile.Profile;
import org.hcgames.icefyre.profile.ProfileState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitBuilderListeners implements Listener {

    private static Icefyre main = Icefyre.getInstance();

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        String[] args = event.getLines();
        String sub = args[1];
        if (args[0].equalsIgnoreCase("[Kit Builder]")) {
            if (sub.equalsIgnoreCase("Items") || sub.equalsIgnoreCase("Spawn")) {
                List<String> newLines = main.getLangFile().getStringList("KIT_BUILDER.SIGN." + (sub.equalsIgnoreCase("Items") ? "ITEMS" : "RETURN_TO_SPAWN"));
                for (int i = 0; i < newLines.size(); i++) {
                    event.setLine(i, newLines.get(i));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (profile != null && profile.getState() == ProfileState.IN_BUILDER && profile.getBuilderProfile() != null && profile.getBuilderProfile().getData() == null) {

                if (block.getState() instanceof Chest) {
                    player.openInventory(profile.getBuilderProfile().getLadder().getKitBuilderInventory());
                    return;
                }

                if (block.getType() == Material.ANVIL) {
                    player.openInventory(profile.getKitSaveInventory(profile.getBuilderProfile().getLadder()));
                }

                if (block.getState() instanceof Sign) {
                    Sign sign = (Sign) block.getState();
                    String[] returnText = main.getLangFile().getStringList("KIT_BUILDER.SIGN.RETURN_TO_SPAWN").toArray(new String[4]);

                    if (Arrays.equals(sign.getLines(), returnText)) {
                        main.getLobby().teleportToSpawn(player);
                        main.getLobby().setupPlayer(player);
                        profile.setBuilderProfile(null);
                        profile.setState(ProfileState.IN_LOBBY);
                        player.sendMessage(main.getLangFile().getString("KIT_BUILDER.RETURNED"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        ItemStack itemStack = event.getCurrentItem();

        if (event.getInventory().getTitle().equalsIgnoreCase(main.getLobby().getKitBuilder().getGraphicalInterface(QueueType.UNRANKED).getTitle())) {
            event.setCancelled(true);
            if (profile != null && profile.getQueueProfile() == null && itemStack != null) {
                for (Queue queue : Queue.getQueues()) {
                    if (queue.getLadder().getIcon().getType() == itemStack.getType() && queue.getLadder().getIcon().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()) && queue.getLadder().getIcon().getDurability() == itemStack.getDurability()) {

                        Party party = profile.getParty();
                        if (party != null) {
                            party.sendMessage(main.getLangFile().getString("PARTY.COMMAND.LEAVE.ANNOUNCEMENT").replace("%PLAYER%", profile.getPlayer().getName()));
                            party.getMembers().remove(profile);
                            main.getFightHandler().setupPlayer(profile.getPlayer());

                            if (party.getMembers().size() == 0) {
                                Party.getParties().remove(party);
                                main.getLobby().setupPlayer(party.getLeader().getPlayer());
                                main.getFightHandler().setupPlayer(party.getLeader().getPlayer());
                            }

                        }

                        profile.setBuilderProfile(new KitBuilderProfile(main.getLobby().getKitBuilder(), queue.getLadder()));
                        profile.setState(ProfileState.IN_BUILDER);
                        player.teleport(profile.getBuilderProfile().getKitBuilder().getLocation());
                        player.sendMessage(main.getLangFile().getString("KIT_BUILDER.SENT"));
                        player.closeInventory();
                        player.getInventory().clear();
                        break;
                    }
                }
            }
        }
    }

    @EventHandler //LOL THIS IS AIDS
    public void onInventoryClickEventKitSave(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);
        ItemStack itemStack = event.getCurrentItem();

        if (profile != null && profile.getBuilderProfile() != null && itemStack != null) {
            if (event.getInventory().getTitle().equalsIgnoreCase(profile.getKitSaveInventory(profile.getBuilderProfile().getLadder()).getTitle())) {
                event.setCancelled(true);

                if (itemStack.getType() == Material.CHEST && event.getInventory().getItem(event.getRawSlot() + 9) == null) {
                    profile.getKits().get(profile.getBuilderProfile().getLadder())[(int) Math.ceil(event.getRawSlot() / 2)] = new Kit(profile.getBuilderProfile().getLadder().getName() + " Kit #" + ((int) Math.ceil(event.getRawSlot() / 2) + 1), profile.getBuilderProfile().getLadder(), new ArrayList<>(Arrays.asList(player.getInventory().getContents())), new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents())));
                    player.sendMessage(main.getLangFile().getString("KIT_BUILDER.CREATED").replace("%NAME%", profile.getBuilderProfile().getLadder().getName() + " Kit #" + ((int) Math.ceil(event.getRawSlot() / 2) + 1)));
                    player.closeInventory();
                    return;
                }

                if (itemStack.getType() == Material.ENCHANTED_BOOK && event.getInventory().getItem(event.getRawSlot() - 9) != null && event.getInventory().getItem(event.getRawSlot() - 9).getType() == Material.CHEST) {
                    Kit kit = profile.getKits().get(profile.getBuilderProfile().getLadder())[(int) Math.ceil((event.getRawSlot() - 9) / 2)];

                    if (kit != null) {
                        kit.setContents(new ArrayList<>(Arrays.asList(player.getInventory().getContents())));
                        kit.setArmor(new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents())));
                        player.sendMessage(main.getLangFile().getString("KIT_BUILDER.MODIFIED").replace("%KIT%", kit.getName()));
                    }

                    player.closeInventory();
                    return;
                }

                if (itemStack.getType() == Material.NAME_TAG && event.getInventory().getItem(event.getRawSlot() - 18) != null && event.getInventory().getItem(event.getRawSlot() - 18).getType() == Material.CHEST) {
                    Kit kit = profile.getKits().get(profile.getBuilderProfile().getLadder())[(int) Math.ceil((event.getRawSlot() - 18) / 2)];

                    if (kit != null) {
                        profile.getBuilderProfile().setData(new KitBuilderProfileData((int) Math.ceil((event.getRawSlot() - 18) / 2)));
                        player.sendMessage(main.getLangFile().getString("KIT_BUILDER.TYPE_IN_CHAT").replace("%KIT%", kit.getName()));
                    }

                    player.closeInventory();
                    return;
                }

                if (itemStack.getType() == Material.FIRE && event.getInventory().getItem(event.getRawSlot() - 27) != null && event.getInventory().getItem(event.getRawSlot() - 27).getType() == Material.CHEST) {
                    Kit kit = profile.getKits().get(profile.getBuilderProfile().getLadder())[(int) Math.ceil((event.getRawSlot() - 27) / 2)];

                    if (kit != null) {
                        player.sendMessage(main.getLangFile().getString("KIT_BUILDER.DELETED").replace("%KIT%", kit.getName()));
                        profile.getKits().get(profile.getBuilderProfile().getLadder())[(int) Math.ceil((event.getRawSlot() - 27) / 2)] = null;

                        for (int i = 0; i < profile.getKits().get(profile.getBuilderProfile().getLadder()).length; i++) {
                            Kit other = profile.getKits().get(profile.getBuilderProfile().getLadder())[i];
                            if (i > 0 && other != null) {

                                if (other.getName().equalsIgnoreCase(profile.getBuilderProfile().getLadder().getName() + " " + "Kit #" + (i + 1))) {
                                    other.setName(profile.getBuilderProfile().getLadder().getName() + " Kit #" + i);
                                }

                                profile.getKits().get(profile.getBuilderProfile().getLadder())[i - 1] = other;
                                profile.getKits().get(profile.getBuilderProfile().getLadder())[i] = null;
                            }
                        }
                    }

                    player.closeInventory();
                }
            }
        }

    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile != null && profile.getBuilderProfile() != null && profile.getBuilderProfile().getData() != null) {
            Kit kit = profile.getKits().get(profile.getBuilderProfile().getLadder())[profile.getBuilderProfile().getData().getPosition()];

            if (kit != null) {
                player.sendMessage(main.getLangFile().getString("KIT_BUILDER.RENAMED").replace("%OLD_NAME%", kit.getName()).replace("%NEW_NAME%", event.getMessage()));
                kit.setName(event.getMessage());
            }

            profile.getBuilderProfile().setData(null);
            event.setCancelled(true);
        }

    }

}
