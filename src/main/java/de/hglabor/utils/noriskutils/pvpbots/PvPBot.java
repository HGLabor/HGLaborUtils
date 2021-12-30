package de.hglabor.utils.noriskutils.pvpbots;


import de.hglabor.utils.noriskutils.NMSUtils;
import de.hglabor.utils.noriskutils.pvpbots.goal.PvPBotMeleeAttackGoal;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
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
    this.goalSelector.addGoal(0, new PvPBotMeleeAttackGoal(this, 1, false));
    if (isDefaultPathfinderTarget) {
      this.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, net.minecraft.world.entity.player.Player.class, true));
    } else {
      this.goalSelector.addGoal(1, new PathfinderFindTarget());
    }
    this.goalSelector.addGoal(2, new FloatGoal(this));  //Jumps out of water
  }

  @Override
  protected boolean shouldDropExperience() {
    return false;
  }


  @Override
  protected SoundEvent getHurtSound(DamageSource source) {
    return source == DamageSource.ON_FIRE ? SoundEvents.PLAYER_HURT_ON_FIRE : (source == DamageSource.DROWN ? SoundEvents.PLAYER_HURT_DROWN : (source == DamageSource.SWEET_BERRY_BUSH ? SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH : (source == DamageSource.FREEZE ? SoundEvents.PLAYER_HURT_FREEZE : SoundEvents.PLAYER_HURT)));
  }

  @Override
  public SoundEvent getDeathSound() {
    return SoundEvents.PLAYER_DEATH;
  }

  public Zombie getEntity() {
    return (Zombie) getBukkitEntity();
  }

  @Override
  protected SoundEvent getStepSound() {
    return SoundEvents.GRASS_STEP;
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return null;
  }

  @Override
  protected SoundEvent getSwimSound() {
    return SoundEvents.PLAYER_SWIM;
  }

  @Override
  protected SoundEvent getSwimSplashSound() {
    return SoundEvents.PLAYER_SPLASH;
  }

  @Override
  protected SoundEvent getSwimHighSpeedSplashSound() {
    return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
  }

  @Override
  protected SoundEvent getFallDamageSound(int distance) {
    return distance > 4 ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
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
