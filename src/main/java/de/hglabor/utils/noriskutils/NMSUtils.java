package de.hglabor.utils.noriskutils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class NMSUtils {
    private NMSUtils() {
    }

    public static World getWorld(Entity entity) {
        return ((CraftEntity) entity).getHandle().t;
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().b.a(packet);
    }

    public static EntityLiving getEntityLiving(LivingEntity entity) {
        return (EntityLiving) ((CraftEntity) entity).getHandle();
    }
}
