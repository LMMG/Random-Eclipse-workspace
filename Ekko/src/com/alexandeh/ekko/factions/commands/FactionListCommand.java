package com.alexandeh.ekko.factions.commands;

import com.alexandeh.ekko.factions.type.PlayerFaction;
import com.alexandeh.ekko.utils.command.Command;
import com.alexandeh.ekko.utils.command.CommandArgs;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Copyright 2016 Alexander Maxwell
 * Use and or redistribution of compiled JAR file and or source code is permitted only if given
 * explicit permission from original author: Alexander Maxwell
 */
public class FactionListCommand extends FactionCommand {
    @Command(name = "f.list", aliases = {"faction.list", "factions.list"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        String[] args = command.getArgs();
        CommandSender sender = command.getSender();

        final HashMap<PlayerFaction, Integer> factions = new HashMap<>();
        int page = 1;

        for (PlayerFaction playerFaction : PlayerFaction.getPlayerFactions()) {
            if (playerFaction.getOnlinePlayers().size() > 0) {
                factions.put(playerFaction, playerFaction.getOnlinePlayers().size());
            }
        }

        List<PlayerFaction> sortedList = new ArrayList<>(factions.keySet());
        Collections.sort(sortedList, new Comparator<PlayerFaction>() {
            @Override
            public int compare(PlayerFaction firstFaction, PlayerFaction secondFaction) {
                return factions.get(firstFaction).compareTo(factions.get(secondFaction));
            }
        });

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) {
                if (NumberUtils.isNumber(args[1])) {
                    page = (int) Double.parseDouble(args[1]);
                }
            }
        }

        if (sortedList.isEmpty()) {
            sender.sendMessage(langConfig.getString("ERROR.NO_FACTIONS_TO_LIST"));
            return;
        }

        int listSize = Math.round(sortedList.size() / 10);
        if (listSize == 0) {
            listSize = 1;
        }

        if (page > listSize) {
            page = listSize;
        }

        for (String msg : langConfig.getStringList("FACTION_LIST")) {
            if (msg.contains("%FACTION%")) {
                for (int i = page * 10 - 10; i < page * 10; i++) {
                    if (sortedList.size() > i) {
                        PlayerFaction playerFaction = sortedList.get(i);
                        FancyMessage fancyMessage = new FancyMessage(msg.replace("%FACTION%", playerFaction.getName()).replace("%DTR%", playerFaction.getDeathsTillRaidable() + "").replace("%MAX_DTR%", playerFaction.getMaxDeathsTillRaidable() + "").replace("%BALANCE%", playerFaction.getBalance() + "").replace("%ONLINE_COUNT%", playerFaction.getOnlinePlayers().size() + "").replace("%MAX_COUNT%", playerFaction.getMembers().size() + 1 + playerFaction.getOfficers().size() + "").replace("%POSITION%", (i + 1) + "")).command("/f show " + playerFaction.getName().toLowerCase());
                        fancyMessage.send(sender);
                    }
                }
            } else {
                sender.sendMessage(msg.replace("%PAGE%", page + "").replace("%TOTAL_PAGES%", listSize + ""));
            }
        }
    }
}
