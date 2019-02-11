package org.hcgames.icefyre.util.packet;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ProjectileUtil {


    @SuppressWarnings("unchecked")
    public static <T extends Projectile> T launchPotion(Player player, Class<? extends T> projectile, Vector velocity, ItemStack itemStack) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        Entity launch = null;

        if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, ((CraftPlayer)player).getHandle(), CraftItemStack.asNMSCopy(itemStack));
        }

        Validate.notNull(launch, "Projectile not supported");

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addEntity(launch);
        return (T) launch.getBukkitEntity();
    }

    public static void hide(Player player, org.bukkit.entity.Entity entity) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entity.getEntityId()));
    }

}
