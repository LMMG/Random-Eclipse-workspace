package gg.vexus.nms;


import net.minecraft.server.v1_7_R4.ChatClickable;
import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.ChatHoverable;
import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TextNMS
        extends ChatComponentText {
    public TextNMS() {
        super("");
    }

    public TextNMS(String string) {
        super(string);
    }

    public TextNMS(Object object) {
        super(String.valueOf(object));
    }

    public TextNMS append(Object object) {
        return append(String.valueOf(object));
    }

    public TextNMS append(String text) {
        return (TextNMS) a(text);
    }

    public TextNMS append(IChatBaseComponent node) {
        return (TextNMS) addSibling(node);
    }

    public TextNMS append(IChatBaseComponent... nodes) {
        IChatBaseComponent[] arrayOfIChatBaseComponent;
        int j = (arrayOfIChatBaseComponent = nodes).length;
        for (int i = 0; i < j; i++) {
            IChatBaseComponent node = arrayOfIChatBaseComponent[i];
            addSibling(node);
        }
        return this;
    }

    public TextNMS setBold(boolean bold) {
        getChatModifier().setBold(Boolean.valueOf(bold));
        return this;
    }

    public TextNMS setItalic(boolean italic) {
        getChatModifier().setItalic(Boolean.valueOf(italic));
        return this;
    }

    public TextNMS setUnderline(boolean underline) {
        getChatModifier().setUnderline(Boolean.valueOf(underline));
        return this;
    }

    public TextNMS setRandom(boolean random) {
        getChatModifier().setRandom(Boolean.valueOf(random));
        return this;
    }

    public TextNMS setStrikethrough(boolean strikethrough) {
        getChatModifier().setStrikethrough(Boolean.valueOf(strikethrough));
        return this;
    }

    public TextNMS setColor(ChatColor color) {
        getChatModifier().setColor(EnumChatFormat.valueOf(color.name()));
        return this;
    }

    public String toRawText() {
        return c();
    }
}

