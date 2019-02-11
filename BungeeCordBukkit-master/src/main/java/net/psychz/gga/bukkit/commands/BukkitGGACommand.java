/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psychz.gga.bukkit.commands;

import net.psychz.gga.bukkit.BukkitSender;
import net.psychz.gga.commands.GGACommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Luca
 */
public class BukkitGGACommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GGACommand.onCommand(new BukkitSender(sender), args);
        return true;
    }

}
