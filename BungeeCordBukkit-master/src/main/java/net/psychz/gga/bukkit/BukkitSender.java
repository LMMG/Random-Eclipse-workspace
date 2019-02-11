package net.psychz.gga.bukkit;

import net.psychz.gga.manager.AbstractSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitSender extends AbstractSender {
    
    private final CommandSender sender;
    
    public BukkitSender(CommandSender sender) {
        super(sender.getName(), (sender instanceof ConsoleCommandSender));
        this.sender = sender;
    }
    
    @Override
    public void sendMessage(TextComponent textComponent) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(textComponent);
        } else {
            sender.sendMessage(textComponent.toLegacyText());
        }
    }
    
    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
    
}
