/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  org.bukkit.ChatColor
 *  org.bukkit.inventory.ItemStack
 */
package com.parapvp.util.chat;

import com.parapvp.util.chat.Text;
import java.util.Collection;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class TextUtils {
    public static Text join(Collection<Text> textCollection, String delimiter) {
        Text result = new Text();
        Text prefix = new Text();
        for (Text text : textCollection) {
            result.append((IChatBaseComponent)prefix).append((IChatBaseComponent)text);
            prefix = new Text(", ");
        }
        return result;
    }

    public static Text joinItemList(Collection<ItemStack> collection, String delimiter, boolean showQuantity) {
        Text text = new Text();
        for (ItemStack stack : collection) {
            if (stack == null) continue;
            text.append((IChatBaseComponent)new Text(delimiter));
            if (showQuantity) {
                text.append((IChatBaseComponent)new Text("[").setColor(ChatColor.YELLOW));
            }
            text.appendItem(stack);
            if (!showQuantity) continue;
            text.append((IChatBaseComponent)new Text(" x" + stack.getAmount()).setColor(ChatColor.YELLOW)).append((IChatBaseComponent)new Text("]").setColor(ChatColor.YELLOW));
        }
        return text;
    }
}

