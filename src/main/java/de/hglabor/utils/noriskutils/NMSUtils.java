package de.hglabor.utils.noriskutils;

import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class NMSUtils {
    private NMSUtils() {
    }

    public static World getWorld(Entity entity) {
        return ((CraftEntity) entity).getHandle().getWorld();
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static EntityLiving getEntityLiving(LivingEntity entity) {
        return (EntityLiving) ((CraftEntity) entity).getHandle();
    }
}
