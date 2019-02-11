/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatClickable
 *  net.minecraft.server.v1_7_R4.ChatHoverable
 *  net.minecraft.server.v1_7_R4.ChatMessage
 *  net.minecraft.server.v1_7_R4.ChatModifier
 *  net.minecraft.server.v1_7_R4.EnumChatFormat
 *  net.minecraft.server.v1_7_R4.EnumClickAction
 *  net.minecraft.server.v1_7_R4.EnumHoverAction
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 */
package com.parapvp.util.chat;

import com.parapvp.util.chat.ChatUtil;
import com.parapvp.util.chat.ClickAction;
import com.parapvp.util.chat.HoverAction;
import com.parapvp.util.chat.Text;
import net.minecraft.server.v1_7_R4.ChatClickable;
import net.minecraft.server.v1_7_R4.ChatHoverable;
import net.minecraft.server.v1_7_R4.ChatMessage;
import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.EnumClickAction;
import net.minecraft.server.v1_7_R4.EnumHoverAction;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class Trans
extends ChatMessage {
    public Trans() {
        super("", new Object[0]);
    }

    public /* varargs */ Trans(String string, Object ... objects) {
        super(string, objects);
    }

    public static Trans fromItemStack(ItemStack stack) {
        return ChatUtil.fromItemStack(stack);
    }

    public IChatBaseComponent f() {
        return this.h();
    }

    public Trans append(Object object) {
        return this.append(String.valueOf(object));
    }

    public Trans append(String text) {
        return (Trans)this.a(text);
    }

    public Trans append(IChatBaseComponent node) {
        return (Trans)this.addSibling(node);
    }

    public /* varargs */ Trans append(IChatBaseComponent ... nodes) {
        for (IChatBaseComponent node : nodes) {
            this.addSibling(node);
        }
        return this;
    }

    public Trans appendItem(ItemStack stack) {
        return this.append((IChatBaseComponent)ChatUtil.fromItemStack(stack));
    }

    public Trans localText(ItemStack stack) {
        return this.append((IChatBaseComponent)ChatUtil.localFromItem(stack));
    }

    public Trans setBold(boolean bold) {
        this.getChatModifier().setBold(Boolean.valueOf(bold));
        return this;
    }

    public Trans setItalic(boolean italic) {
        this.getChatModifier().setItalic(Boolean.valueOf(italic));
        return this;
    }

    public Trans setUnderline(boolean underline) {
        this.getChatModifier().setUnderline(Boolean.valueOf(underline));
        return this;
    }

    public Trans setRandom(boolean random) {
        this.getChatModifier().setRandom(Boolean.valueOf(random));
        return this;
    }

    public Trans setStrikethrough(boolean strikethrough) {
        this.getChatModifier().setStrikethrough(Boolean.valueOf(strikethrough));
        return this;
    }

    public Trans setColor(ChatColor color) {
        this.getChatModifier().setColor(EnumChatFormat.valueOf((String)color.name()));
        return this;
    }

    public Trans setClick(ClickAction action, String value) {
        this.getChatModifier().setChatClickable(new ChatClickable(action.getNMS(), value));
        return this;
    }

    public Trans setHover(HoverAction action, IChatBaseComponent value) {
        this.getChatModifier().a(new ChatHoverable(action.getNMS(), value));
        return this;
    }

    public Trans setHoverText(String text) {
        return this.setHover(HoverAction.SHOW_TEXT, (IChatBaseComponent)new Text(text));
    }

    public Trans reset() {
        ChatUtil.reset((IChatBaseComponent)this);
        return this;
    }

    public String toRawText() {
        return this.c();
    }

    public void send(CommandSender sender) {
        ChatUtil.send(sender, (IChatBaseComponent)this);
    }
}

