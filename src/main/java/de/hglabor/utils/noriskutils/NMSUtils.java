package de.hglabor.utils.noriskutils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class NMSUtils {
  private NMSUtils() {
  }

  public static Level getWorld(Entity entity) {
    return ((CraftEntity) entity).getHandle().level;
  }

  public static void sendPacket(Player player, Packet<?> packet) {
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

  public static net.minecraft.world.entity.LivingEntity getEntityLiving(LivingEntity entity) {
    return (net.minecraft.world.entity.LivingEntity) ((CraftEntity) entity).getHandle();
  }
}
