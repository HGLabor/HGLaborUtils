package de.hglabor.utils.noriskutils.pvpbots;

import de.hglabor.utils.noriskutils.NMSUtils;
import de.hglabor.utils.noriskutils.pvpbots.nms.LaborPathfinderFindTarget;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;

public class FlyingPvPBot extends EntityBat {
    public final static String META_DATA = "pvpBot";
    private final String name;
    private final PlayerDisguise playerDisguise;
    protected EntityLiving target;
    protected double attackRange = 10; //SQUARE ROOT
    private boolean isDefaultPathfinderTarget;

    public FlyingPvPBot(World world, String name, Player player, JavaPlugin plugin) {
        this(world, name, NMSUtils.getEntityLiving(player), plugin);
    }

    public FlyingPvPBot(World world, String name, JavaPlugin plugin) {
        super(EntityTypes.BAT, ((CraftWorld) world).getHandle());
        this.name = name;
        this.getWorld().addEntity(this);
        this.getEntity().setMetadata(META_DATA, new FixedMetadataValue(plugin, ""));
        this.getEntity().setShouldBurnInDay(false);
        this.getEntity().setRemoveWhenFarAway(false);
        this.setCustomName(new ChatComponentText(name));
        this.setCustomNameVisible(true);
        this.playerDisguise = new PlayerDisguise(name);
        this.playerDisguise.setNameVisible(true);
        this.playerDisguise.setDisplayedInTab(false);
        this.isDefaultPathfinderTarget = true;
        this.initPathfinder();
        DisguiseAPI.disguiseEntity(this.getBukkitEntity(), playerDisguise);
    }

    public FlyingPvPBot(World world, String name, EntityLiving target, JavaPlugin plugin) {
        super(EntityTypes.BAT, ((CraftWorld) world).getHandle());
        this.name = name;
        this.target = target;
        this.getWorld().addEntity(this);
        this.getEntity().setMetadata(META_DATA, new FixedMetadataValue(plugin, ""));
        this.getEntity().setShouldBurnInDay(false);
        this.getEntity().setRemoveWhenFarAway(false);
        this.setCustomName(new ChatComponentText(name));
        this.setCustomNameVisible(true);
        this.playerDisguise = new PlayerDisguise(name);
        this.playerDisguise.setNameVisible(true);
        DisguiseAPI.disguiseEntity(this.getBukkitEntity(), playerDisguise);
        this.setGoalTarget(target, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
    }

    @Override
    public void die(DamageSource damagesource) {
        super.die(damagesource);
    }

    @Override
    public MinecraftKey getLootTable() {
        return null;
    }

    @Override
    protected MinecraftKey getDefaultLootTable() {
        return null;
    }

    public void spawn(Location location) {
        this.setPosition(location.getX(), location.getY(), location.getZ());
    }

    /**
     * @param range will be hoch 2 genommen
     */
    public FlyingPvPBot withRange(double range) {
        this.attackRange = range * range;
        return this;
    }

    public FlyingPvPBot withItemInSlot(EquipmentSlot slot, ItemStack itemStack) {
        EntityEquipment equipment = this.getEntity().getEquipment();
        if (equipment != null) {
            equipment.setItem(slot, itemStack);
        }
        return this;
    }

    public FlyingPvPBot withMovementSpeed(double speed) {
        AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (attribute != null) {
            attribute.setBaseValue(speed);
        }
        return this;
    }

    public FlyingPvPBot withSkin(String skin) {
        this.playerDisguise.setSkin(skin);
        return this;
    }

    public FlyingPvPBot withDisplayInTab(boolean value) {
        this.playerDisguise.setDisplayedInTab(value);
        return this;
    }

    public FlyingPvPBot withFollowRange(double followRange) {
        AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
        if (attribute != null) {
            attribute.setBaseValue(followRange);
        }
        return this;
    }

    public FlyingPvPBot withAttribute(Attribute attribute, double value) {
        AttributeInstance attributeInstance = this.craftAttributes.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue(value);
        }
        return this;
    }

    public FlyingPvPBot withHealth(int health) {
        AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(health);
        }
        this.setHealth(health);
        return this;
    }

    public FlyingPvPBot withDefaultPathfinderTarget(boolean value) {
        this.isDefaultPathfinderTarget = value;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean a(EntityHuman var0) {
        return true;
    }

    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalMeleeAttack(1));
        if (isDefaultPathfinderTarget) {
            this.goalSelector.a(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
            this.targetSelector.a(0, new LaborPathfinderFindTarget(this, null));
        } else {
            this.goalSelector.a(1, new PathfinderFindTarget());
        }
        this.goalSelector.a(2, new PathfinderGoalFloat(this));  //Jumps out of water
    }

    @Override
    protected boolean isDropExperience() {
        return false;
    }

    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return damagesource == DamageSource.BURN ? SoundEffects.ENTITY_PLAYER_HURT_ON_FIRE : (damagesource == DamageSource.DROWN ? SoundEffects.ENTITY_PLAYER_HURT_DROWN : (damagesource == DamageSource.SWEET_BERRY_BUSH ? SoundEffects.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH : SoundEffects.ENTITY_PLAYER_HURT));
    }

    public Bat getEntity() {
        return (Bat) getBukkitEntity();
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PLAYER_DEATH;
    }

    protected SoundEffect getSoundFall(int i) {
        return i > 4 ? SoundEffects.ENTITY_PLAYER_BIG_FALL : SoundEffects.ENTITY_PLAYER_SMALL_FALL;
    }

    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_PLAYER_SWIM;
    }

    protected SoundEffect getSoundSplash() {
        return SoundEffects.ENTITY_PLAYER_SPLASH;
    }

    protected SoundEffect getSoundSplashHighSpeed() {
        return SoundEffects.ENTITY_PLAYER_SPLASH_HIGH_SPEED;
    }


    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }


    private class PathfinderGoalMeleeAttack extends PathfinderGoal {
        private final double b; // dont know what this does
        private final boolean c; // dont know what this does
        private PathEntity pathEntity;
        private double e;
        private double f;
        private double g;
        private int h;
        private int attackCooldown;
        private long k;

        public PathfinderGoalMeleeAttack(double var1) {
            this.b = var1;
            this.c = true;
            this.a(EnumSet.of(Type.MOVE, Type.LOOK));
        }

        public boolean a() {
            long var0 = FlyingPvPBot.this.world.getTime();
            if (var0 - this.k < 20L) {
                return false;
            } else {
                this.k = var0;
                EntityLiving var2 = FlyingPvPBot.this.getGoalTarget();
                if (var2 == null) {
                    return false;
                } else if (!var2.isAlive()) {
                    return false;
                } else {
                    this.pathEntity = FlyingPvPBot.this.getNavigation().a(var2, 0);
                    if (this.pathEntity != null) {
                        return true;
                    } else {
                        return this.attackEntity(var2) >= FlyingPvPBot.this.h(var2.locX(), var2.locY(), var2.locZ());
                    }
                }
            }
        }

        public boolean b() {
            EntityLiving var0 = FlyingPvPBot.this.getGoalTarget();
            if (var0 == null) {
                return false;
            } else if (!var0.isAlive()) {
                return false;
            } else if (!this.c) {
                return !FlyingPvPBot.this.getNavigation().m();
            } else if (!FlyingPvPBot.this.a(var0.getChunkCoordinates())) {
                return false;
            } else {
                return !(var0 instanceof EntityHuman) || !var0.isSpectator() && !((EntityHuman) var0).isCreative();
            }
        }

        public void c() {
            FlyingPvPBot.this.getNavigation().a(this.pathEntity, this.b);
            FlyingPvPBot.this.setAggressive(true);
            this.h = 0;
            this.attackCooldown = 0;
        }

        public void d() {
            EntityLiving var0 = FlyingPvPBot.this.getGoalTarget();
            if (!IEntitySelector.e.test(var0)) {
                FlyingPvPBot.this.setGoalTarget(null);
            }

            FlyingPvPBot.this.setAggressive(false);
            FlyingPvPBot.this.getNavigation().o();
        }

        public void e() {
            EntityLiving target = FlyingPvPBot.this.getGoalTarget();
            FlyingPvPBot.this.getControllerLook().a(target, 30.0F, 30.0F);
            double distanceToTarget = FlyingPvPBot.this.h(target.locX(), target.locY(), target.locZ());
            this.h = Math.max(this.h - 1, 0);
            if ((this.c || FlyingPvPBot.this.getEntitySenses().a(target)) && this.h <= 0 && (this.e == 0.0D && this.f == 0.0D && this.g == 0.0D || target.h(this.e, this.f, this.g) >= 1.0D || FlyingPvPBot.this.getRandom().nextFloat() < 0.05F)) {
                this.e = target.locX();
                this.f = target.locY();
                this.g = target.locZ();
                this.h = 4 + FlyingPvPBot.this.getRandom().nextInt(7);
                if (distanceToTarget > 1024.0D) {
                    this.h += 10;
                } else if (distanceToTarget > 256.0D) {
                    this.h += 5;
                }

                if (!FlyingPvPBot.this.getNavigation().a(target, this.b)) {
                    this.h += 15;
                }
            }

            this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
            this.attackEntity(target, distanceToTarget);
        }

        protected void attackEntity(EntityLiving target, double distanceToTarget) {
            if (distanceToTarget <= attackRange && this.attackCooldown <= 0) {
                this.resetAttackCooldown();
                FlyingPvPBot.this.swingHand(EnumHand.MAIN_HAND);
                FlyingPvPBot.this.attackEntity(target);
            }
        }

        protected void resetAttackCooldown() {
            this.attackCooldown = 0;
        }

        protected double attackEntity(EntityLiving var0) {
            return FlyingPvPBot.this.getWidth() * 2.0F * FlyingPvPBot.this.getWidth() * 2.0F + var0.getWidth();
        }
    }

    private class PathfinderFindTarget extends PathfinderGoal {
        @Override
        public boolean a() {
            if (target == null) {
                return false;
            }
            FlyingPvPBot.this.setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
            return true;
        }

        @Override
        public void e() {
        }

        @Override
        public boolean b() {
            return false;
        }
    }
}
