package com.parapvp.base.manager;

import java.util.function.Consumer;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Entity;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.List;
import org.bukkit.entity.Item;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.events.PacketContainer;
import com.parapvp.base.BasePlugin;
import com.parapvp.base.user.BaseUser;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.parapvp.base.user.UserManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.inventory.ItemStack;

public class ProtocolHook
{
    private static final ItemStack AIR;
    
    public static void hook(final BasePlugin basePlugin) {
        final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        final UserManager userManager = basePlugin.getUserManager();
        protocolManager.addPacketListener((PacketListener)new PacketAdapter(basePlugin, new PacketType[] { PacketType.Play.Server.ENTITY_EQUIPMENT }) {
            public void onPacketSending(final PacketEvent event) {
                if (!basePlugin.getServerHandler().useProtocolLib) {
                    return;
                }
                final Player player = event.getPlayer();
                final BaseUser baseUser = userManager.getUser(player.getUniqueId());
                if (!baseUser.isGlintEnabled()) {
                    final PacketContainer packet = event.getPacket();
                    final StructureModifier<ItemStack> modifier = (StructureModifier<ItemStack>)packet.getItemModifier();
                    if (modifier.size() > 0) {
                        final ItemStack stack = (ItemStack)modifier.read(0);
                        if (stack != null && stack.getType() != Material.AIR) {
                            convert(stack);
                        }
                    }
                }
            }
        });
        protocolManager.addPacketListener((PacketListener)new PacketAdapter(basePlugin, new PacketType[] { PacketType.Play.Server.ENTITY_METADATA }) {
            public void onPacketSending(final PacketEvent event) {
                if (!basePlugin.getServerHandler().useProtocolLib) {
                    return;
                }
                final Player player = event.getPlayer();
                final BaseUser baseUser = userManager.getUser(player.getUniqueId());
                if (!baseUser.isGlintEnabled()) {
                    final PacketContainer packet = event.getPacket();
                    final StructureModifier<Entity> modifier = (StructureModifier<Entity>)packet.getEntityModifier(event);
                    if (modifier.size() > 0 && modifier.read(0) instanceof Item) {
                        final WrappedDataWatcher watcher = new WrappedDataWatcher((List)packet.getWatchableCollectionModifier().read(0));
                        if (watcher.size() >= 10) {
                            final ItemStack stack = watcher.getItemStack(10).clone();
                            if (stack != null && stack.getType() != Material.AIR) {
                                convert(stack);
                            }
                        }
                    }
                }
            }
        });
    }
    
    private static ItemStack convert(final ItemStack origin) {
        if (origin == null || origin.getType() == Material.AIR) {
            return origin;
        }
        switch (origin.getType()) {
            case POTION:
            case GOLDEN_APPLE: {
                if (origin.getDurability() > 0) {
                    origin.setDurability((short)0);
                    break;
                }
                break;
            }
            case ENCHANTED_BOOK: {
                origin.setType(Material.BOOK);
                break;
            }
            default: {
                origin.getEnchantments().keySet().forEach(origin::removeEnchantment);
                break;
            }
        }
        return origin;
    }
    
    static {
        AIR = new ItemStack(Material.AIR, 1);
    }
}