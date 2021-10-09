package de.hglabor.utils.noriskutils.pvpbots.nms;

import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LaborPathfinderFindTarget extends PathfinderGoal {
    public EntityInsentient mob;
    public UUID safe;
    public boolean attack;

    public LaborPathfinderFindTarget(EntityInsentient mob, UUID safe) {
        this.mob = mob;
        this.safe = safe;
    }

    @Override
    public boolean a() {

        Map<Double, EntityLiving> p = new HashMap<>();
        double least = 100D;

        for (Entity ent : mob.getBukkitEntity().getNearbyEntities(10, 10, 10)) {
            if (ent instanceof Player) {
                Player target = (Player) ent;
                // Checks
                if (target.getGameMode() == GameMode.CREATIVE || !mob.getEntitySenses().a(((CraftPlayer) target).getHandle())) {
                    continue;
                }
                p.put(ent.getLocation().distance(mob.getBukkitEntity().getLocation()), ((CraftPlayer) ent).getHandle());
            }
        }

        for (Double d : p.keySet()) {
            if (d < least) {
                least = d;
            }
        }

        EntityLiving target = p.get(least);

        if (target == null) {
            mob.setGoalTarget(null, EntityTargetEvent.TargetReason.CUSTOM, true);
            return false;
        }


        if (!(target instanceof EntityPlayer)) {
            mob.setGoalTarget(null, EntityTargetEvent.TargetReason.CUSTOM, true);
            return false;
        }

        mob.setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
        return true;
    }

    @Override
    public void e() {
        if (mob.getGoalTarget() == null) {
            return;
        }

        mob.getNavigation().a(mob.getGoalTarget().getBukkitEntity().getLocation().getX(),
                mob.getGoalTarget().getBukkitEntity().getLocation().getY() + 1.0D,
                mob.getGoalTarget().getBukkitEntity().getLocation().getZ(), 1.5F);

        if (mob.getBukkitEntity().getLocation().distance(mob.getGoalTarget().getBukkitEntity().getLocation()) <= 1.5D) {
            if (mob.getEntitySenses().a(mob.getGoalTarget())) { // canSee method
                if (safe != null) {
                    ((LivingEntity) mob.getGoalTarget().getBukkitEntity()).damage(4.0D, Bukkit.getPlayer(safe));
                } else {
                    ((LivingEntity) mob.getGoalTarget().getBukkitEntity()).damage(4.0D, mob.getBukkitEntity());
                }
            }
        }
    }

    @Override
    public boolean b() {
        return false;
    }
}
