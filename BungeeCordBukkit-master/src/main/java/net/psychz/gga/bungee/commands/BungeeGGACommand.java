/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psychz.gga.bungee.commands;

import net.psychz.gga.bungee.BungeeSender;
import net.psychz.gga.commands.GGACommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 *
 * @author Luca
 */
public class BungeeGGACommand extends Command {

    public BungeeGGACommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        GGACommand.onCommand(new BungeeSender(sender), args);
    }

}
