package net.psychz.gga.bungee;

import net.psychz.gga.manager.AbstractSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.ConsoleCommandSender;

public class BungeeSender extends AbstractSender {

    private final CommandSender sender;

    public BungeeSender(CommandSender sender) {
        super(sender.getName(), (sender instanceof ConsoleCommandSender));
        this.sender = sender;
    }

    @Override
    public void sendMessage(TextComponent textComponent) {
        sender.sendMessage(textComponent);
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

}
