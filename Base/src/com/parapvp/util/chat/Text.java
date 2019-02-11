/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatClickable
 *  net.minecraft.server.v1_7_R4.ChatComponentText
 *  net.minecraft.server.v1_7_R4.ChatHoverable
 *  net.minecraft.server.v1_7_R4.ChatModifier
 *  net.minecraft.server.v1_7_R4.EnumChatFormat
 *  net.minecraft.server.v1_7_R4.EnumClickAction
 *  net.minecraft.server.v1_7_R4.EnumHoverAction
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.parapvp.util.chat;

import com.parapvp.util.chat.ChatUtil;
import com.parapvp.util.chat.ClickAction;
import com.parapvp.util.chat.HoverAction;
import com.parapvp.util.chat.Trans;
import net.minecraft.server.v1_7_R4.ChatClickable;
import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.ChatHoverable;
import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.EnumClickAction;
import net.minecraft.server.v1_7_R4.EnumHoverAction;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Text
extends ChatComponentText {
    public Text() {
        super("");
    }

    public Text(String string) {
        super(string);
    }

    public Text(Object object) {
        super(String.valueOf(object));
    }

    public static Trans fromItemStack(ItemStack stack) {
        return ChatUtil.fromItemStack(stack);
    }

    public Text append(Object object) {
        return this.append(String.valueOf(object));
    }

    public Text append(String text) {
        return (Text)this.a(text);
    }

    public Text append(IChatBaseComponent node) {
        return (Text)this.addSibling(node);
    }

    public /* varargs */ Text append(IChatBaseComponent ... nodes) {
        for (IChatBaseComponent node : nodes) {
            this.addSibling(node);
        }
        return this;
    }

    public Text localText(ItemStack stack) {
        return this.append((IChatBaseComponent)ChatUtil.localFromItem(stack));
    }

    public Text appendItem(ItemStack stack) {
        return this.append((IChatBaseComponent)ChatUtil.fromItemStack(stack));
    }

    public Text setBold(boolean bold) {
        this.getChatModifier().setBold(Boolean.valueOf(bold));
        return this;
    }

    public Text setItalic(boolean italic) {
        this.getChatModifier().setItalic(Boolean.valueOf(italic));
        return this;
    }

    public Text setUnderline(boolean underline) {
        this.getChatModifier().setUnderline(Boolean.valueOf(underline));
        return this;
    }

    public Text setRandom(boolean random) {
        this.getChatModifier().setRandom(Boolean.valueOf(random));
        return this;
    }

    public Text setStrikethrough(boolean strikethrough) {
        this.getChatModifier().setStrikethrough(Boolean.valueOf(strikethrough));
        return this;
    }

    public Text setColor(ChatColor color) {
        this.getChatModifier().setColor(EnumChatFormat.valueOf((String)color.name()));
        return this;
    }

    public Text setClick(ClickAction action, String value) {
        this.getChatModifier().setChatClickable(new ChatClickable(action.getNMS(), value));
        return this;
    }

    public Text setHover(HoverAction action, IChatBaseComponent value) {
        this.getChatModifier().a(new ChatHoverable(action.getNMS(), value));
        return this;
    }

    public Text setHoverText(String text) {
        return this.setHover(HoverAction.SHOW_TEXT, (IChatBaseComponent)new Text(text));
    }

    public Text reset() {
        ChatUtil.reset((IChatBaseComponent)this);
        return this;
    }

    public IChatBaseComponent f() {
        return this.h();
    }

    public String toRawText() {
        return this.c();
    }

    public void send(CommandSender sender) {
        ChatUtil.send(sender, (IChatBaseComponent)this);
    }

    public void broadcast() {
        this.broadcast(null);
    }

    public void broadcast(String permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (permission != null && !player.hasPermission(permission)) continue;
            this.send((CommandSender)player);
        }
        this.send((CommandSender)Bukkit.getConsoleSender());
    }
}

