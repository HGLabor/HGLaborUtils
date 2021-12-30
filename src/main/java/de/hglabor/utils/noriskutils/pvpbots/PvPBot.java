package de.hglabor.utils.noriskutils.pvpbots;


import de.hglabor.utils.noriskutils.NMSUtils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumSet;


public class PvPBot extends net.minecraft.world.entity.monster.Zombie {
  public final static String META_DATA = "pvpBot";
  private final String name;
  private final PlayerDisguise playerDisguise;
  protected LivingEntity target;
  protected double attackRange = 10; //SQUARE ROOT
  private boolean isDefaultPathfinderTarget;

  public PvPBot(World world, String name, Player player, JavaPlugin plugin) {
    this(world, name, NMSUtils.getEntityLiving(player), plugin);
  }

  public PvPBot(World world, String name, JavaPlugin plugin) {
    super(((CraftWorld) world).getHandle());
    this.name = name;
    this.level.addFreshEntity(this);
    this.getEntity().setMetadata(META_DATA, new FixedMetadataValue(plugin, ""));
    this.getEntity().setShouldBurnInDay(false);
    this.getEntity().setRemoveWhenFarAway(false);
    this.setCustomName(new TextComponent(name));
    this.setCustomNameVisible(true);
    this.playerDisguise = new PlayerDisguise(name);
    this.playerDisguise.setNameVisible(true);
    this.playerDisguise.setDisplayedInTab(false);
    this.isDefaultPathfinderTarget = true;
    this.initPathfinder();
    DisguiseAPI.disguiseEntity(this.getBukkitEntity(), playerDisguise);
  }

  public PvPBot(World world, String name, LivingEntity target, JavaPlugin plugin) {
    super(((CraftWorld) world).getHandle());
    this.name = name;
    this.target = target;
    this.level.addFreshEntity(this);
    this.getEntity().setMetadata(META_DATA, new FixedMetadataValue(plugin, ""));
    this.getEntity().setShouldBurnInDay(false);
    this.getEntity().setRemoveWhenFarAway(false);
    this.setCustomName(new TextComponent(name));
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
  public ResourceLocation getDefaultLootTable() {
    return null;
  }

  public void spawn(Location location) {
    this.setPos(location.getX(), location.getY(), location.getZ());
  }

  public PvPBot withRange(double range) {
    this.attackRange = range * range;
    return this;
  }

  public PvPBot withItemInSlot(EquipmentSlot slot, ItemStack itemStack) {
    EntityEquipment equipment = this.getEntity().getEquipment();
    if (equipment != null) {
      equipment.setItem(slot, itemStack);
    }
    return this;
  }

  public PvPBot withMovementSpeed(double speed) {
    AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
    if (attribute != null) {
      attribute.setBaseValue(speed);
    }
    return this;
  }

  public PvPBot withSkin(String skin) {
    this.playerDisguise.setSkin(skin);
    return this;
  }

  public PvPBot withDisplayInTab(boolean value) {
    this.playerDisguise.setDisplayedInTab(value);
    return this;
  }

  public PvPBot withFollowRange(double followRange) {
    AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
    if (attribute != null) {
      attribute.setBaseValue(followRange);
    }
    return this;
  }

  public PvPBot withHealth(int health) {
    AttributeInstance attribute = this.craftAttributes.getAttribute(Attribute.GENERIC_MAX_HEALTH);
    if (attribute != null) {
      attribute.setBaseValue(health);
    }
    this.setHealth(health);
    return this;
  }

  public PvPBot withDefaultPathfinderTarget(boolean value) {
    this.isDefaultPathfinderTarget = value;
    return this;
  }

  protected void initPathfinder() {
    this.goalSelector.addGoal(0, new PathfinderGoalMeleeAttack(1));
    if (isDefaultPathfinderTarget) {
      this.goalSelector.addGoal(5, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    } else {
      this.goalSelector.addGoal(1, new PathfinderFindTarget());
    }
    this.goalSelector.addGoal(2, new PathfinderGoalFloat(this));  //Jumps out of water
  }

  @Override
  protected boolean isDropExperience() {
    return false;
  }

  protected SoundEffect getSoundHurt(DamageSource damagesource) {
    return damagesource == DamageSource.BURN ? SoundEffects.ENTITY_PLAYER_HURT_ON_FIRE : (damagesource == DamageSource.DROWN ? SoundEffects.ENTITY_PLAYER_HURT_DROWN : (damagesource == DamageSource.SWEET_BERRY_BUSH ? SoundEffects.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH : SoundEffects.ENTITY_PLAYER_HURT));
  }

  public Zombie getEntity() {
    return (Zombie) getBukkitEntity();
  }

  @Override
  protected SoundEffect getSoundDeath() {
    return SoundEffects.ENTITY_PLAYER_DEATH;
  }

  @Override
  protected SoundEffect getSoundStep() {
    return SoundEffects.BLOCK_GRASS_STEP;
  }

  @Override
  protected SoundEffect getSoundAmbient() {
    return null;
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
      long var0 = PvPBot.this.world.getTime();
      if (var0 - this.k < 20L) {
        return false;
      } else {
        this.k = var0;
        EntityLiving var2 = PvPBot.this.getGoalTarget();
        if (var2 == null) {
          return false;
        } else if (!var2.isAlive()) {
          return false;
        } else {
          this.pathEntity = PvPBot.this.getNavigation().a(var2, 0);
          if (this.pathEntity != null) {
            return true;
          } else {
            return this.attackEntity(var2) >= PvPBot.this.h(var2.locX(), var2.locY(), var2.locZ());
          }
        }
      }
    }

    public boolean b() {
      EntityLiving var0 = PvPBot.this.getGoalTarget();
      if (var0 == null) {
        return false;
      } else if (!var0.isAlive()) {
        return false;
      } else if (!this.c) {
        return !PvPBot.this.getNavigation().m();
      } else if (!PvPBot.this.a(var0.getChunkCoordinates())) {
        return false;
      } else {
        return !(var0 instanceof EntityHuman) || !var0.isSpectator() && !((EntityHuman) var0).isCreative();
      }
    }

    public void c() {
      PvPBot.this.getNavigation().a(this.pathEntity, this.b);
      PvPBot.this.setAggressive(true);
      this.h = 0;
      this.attackCooldown = 0;
    }

    public void d() {
      EntityLiving var0 = PvPBot.this.getGoalTarget();
      if (!IEntitySelector.e.test(var0)) {
        PvPBot.this.setGoalTarget(null);
      }

      PvPBot.this.setAggressive(false);
      PvPBot.this.getNavigation().o();
    }

    public void e() {
      EntityLiving target = PvPBot.this.getGoalTarget();
      PvPBot.this.getControllerLook().a(target, 30.0F, 30.0F);
      double distanceToTarget = PvPBot.this.h(target.locX(), target.locY(), target.locZ());
      this.h = Math.max(this.h - 1, 0);
      if ((this.c || PvPBot.this.getEntitySenses().a(target)) && this.h <= 0 && (this.e == 0.0D && this.f == 0.0D && this.g == 0.0D || target.h(this.e, this.f, this.g) >= 1.0D || PvPBot.this.getRandom().nextFloat() < 0.05F)) {
        this.e = target.locX();
        this.f = target.locY();
        this.g = target.locZ();
        this.h = 4 + PvPBot.this.getRandom().nextInt(7);
        if (distanceToTarget > 1024.0D) {
          this.h += 10;
        } else if (distanceToTarget > 256.0D) {
          this.h += 5;
        }

        if (!PvPBot.this.getNavigation().a(target, this.b)) {
          this.h += 15;
        }
      }

      this.attackCooldown = Math.max(this.attackCooldown - 1, 0);
      this.attackEntity(target, distanceToTarget);
    }

    protected void attackEntity(EntityLiving target, double distanceToTarget) {
      if (distanceToTarget <= attackRange && this.attackCooldown <= 0) {
        this.resetAttackCooldown();
        PvPBot.this.swingHand(EnumHand.MAIN_HAND);
        PvPBot.this.attackEntity(target);
      }
    }

    protected void resetAttackCooldown() {
      this.attackCooldown = 0;
    }

    protected double attackEntity(EntityLiving var0) {
      return PvPBot.this.getWidth() * 2.0F * PvPBot.this.getWidth() * 2.0F + var0.getWidth();
    }
  }

  private class PathfinderFindTarget extends Goal {
    @Override
    public boolean canUse() {
      if (target == null) {
        return false;
      }
      PvPBot.this.setGoalTarget(target, EntityTargetEvent.TargetReason.CUSTOM, true);
      return true;
    }

    @Override
    public void start() {
    }

    @Override
    public boolean isInterruptable() {
      return false;
    }
  }
}
