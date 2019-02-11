/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatClickable
 *  net.minecraft.server.v1_7_R4.ChatComponentText
 *  net.minecraft.server.v1_7_R4.ChatHoverable
 *  net.minecraft.server.v1_7_R4.ChatModifier
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.EnumChatFormat
 *  net.minecraft.server.v1_7_R4.EnumItemRarity
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  net.minecraft.server.v1_7_R4.Item
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutChat
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.apache.commons.lang3.text.WordUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionType
 */
package com.parapvp.util.chat;

import com.parapvp.util.chat.HoverAction;
import com.parapvp.util.chat.Trans;
import net.minecraft.server.v1_7_R4.ChatClickable;
import net.minecraft.server.v1_7_R4.ChatComponentText;
import net.minecraft.server.v1_7_R4.ChatHoverable;
import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.EnumItemRarity;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ChatUtil {
    public static String getName(net.minecraft.server.v1_7_R4.ItemStack stack) {
        NBTTagCompound nbttagcompound;
        if (stack.tag != null && stack.tag.hasKeyOfType("display", 10) && (nbttagcompound = stack.tag.getCompound("display")).hasKeyOfType("Name", 8)) {
            return nbttagcompound.getString("Name");
        }
        return stack.getItem().a(stack) + ".name";
    }

    public static Trans localFromItem(ItemStack stack) {
        PotionType type;
        Potion potion;
        if (stack.getType() == Material.POTION && stack.getData().getData() == 0 && (potion = Potion.fromItemStack((ItemStack)stack)) != null && (type = potion.getType()) != null && type != PotionType.WATER) {
            String effectName = (potion.isSplash() ? "Splash " : "") + WordUtils.capitalizeFully((String)type.name().replace('_', ' ')) + " L" + potion.getLevel();
            return ChatUtil.fromItemStack(stack).append(" of " + effectName);
        }
        return ChatUtil.fromItemStack(stack);
    }

    public static Trans fromItemStack(ItemStack stack) {
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy((ItemStack)stack);
        NBTTagCompound tag = new NBTTagCompound();
        nms.save(tag);
        return new Trans(ChatUtil.getName(nms), new Object[0]).setColor(ChatColor.getByChar((char)nms.w().e.getChar())).setHover(HoverAction.SHOW_ITEM, (IChatBaseComponent)new ChatComponentText(tag.toString()));
    }

    public static void reset(IChatBaseComponent text) {
        ChatModifier modifier = text.getChatModifier();
        modifier.a((ChatHoverable)null);
        modifier.setChatClickable(null);
        modifier.setBold(Boolean.valueOf(false));
        modifier.setColor(EnumChatFormat.RESET);
        modifier.setItalic(Boolean.valueOf(false));
        modifier.setRandom(Boolean.valueOf(false));
        modifier.setStrikethrough(Boolean.valueOf(false));
        modifier.setUnderline(Boolean.valueOf(false));
    }

    public static void send(CommandSender sender, IChatBaseComponent text) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            PacketPlayOutChat packet = new PacketPlayOutChat(text, true);
            EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
            entityPlayer.playerConnection.sendPacket((Packet)packet);
        } else {
            sender.sendMessage(text.c());
        }
    }
}

