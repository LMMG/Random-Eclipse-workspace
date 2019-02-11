/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psychz.gga.manager;

import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author Luca
 */
public abstract class AbstractSender {

    private final String name;
    private final boolean console;

    public AbstractSender(String name, boolean console) {
        this.name = name;
        this.console = console;
    }

    public String getName() {
        return this.name;
    }

    public boolean isConsole() {
        return this.console;
    }

    public abstract void sendMessage(TextComponent textComponent);

    public abstract void sendMessage(String message);

}
