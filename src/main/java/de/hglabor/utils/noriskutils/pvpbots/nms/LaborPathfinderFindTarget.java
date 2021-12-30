package de.hglabor.utils.noriskutils.pvpbots.nms;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

public class LaborPathfinderFindTarget extends Goal {
  public Mob mob;
  public UUID safe;
  public boolean attack;

  public LaborPathfinderFindTarget(Mob mob, UUID safe, boolean attack) {
    this.mob = mob;
    this.safe = safe;
    this.attack = attack;
  }

  @Override
  public boolean canUse() {
    LivingEntity mobTarget = mob.getTarget();
    if (mobTarget == null) {
      mob.setTarget(null);
      return false;
    }
    Entity target = mobTarget.getBukkitEntity();

    if (!(((CraftLivingEntity) target).getHandle() instanceof Player)) {
      mob.setTarget(null);
      return false;
    }

    if (target.getUniqueId().equals(safe)) {
      mob.setTarget(null);
      return false;
    }

    mob.setGoalTarget(((CraftPlayer) target).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, true);
    return true;
  }

  @Override
  public void start() {
    if (mob.getTarget() == null) {
      return;
    }

    Location location = mob.getTarget().getBukkitEntity().getLocation();
    mob.getNavigation().moveTo(location.getX(), location.getY() + 1.0D, location.getZ(), 1.5F);

    if (!attack) { // We want the entity to not attack but able to move from the method above.
      return;
    }

    if (mob.getBukkitEntity().getLocation().distance(mob.getTarget().getBukkitEntity().getLocation()) <= 1.5D) {
      if (mob.getSensing().hasLineOfSight(mob.getTarget())) { // canSee method
        ((org.bukkit.entity.LivingEntity) mob.getTarget().getBukkitEntity()).damage(4.0D, Bukkit.getPlayer(safe));
      }
    }
  }

  @Override
  public boolean isInterruptable() {
    return false;
  }
}
