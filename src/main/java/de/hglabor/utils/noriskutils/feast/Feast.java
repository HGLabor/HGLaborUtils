package de.hglabor.utils.noriskutils.feast;

import com.google.common.collect.ImmutableMap;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.regions.CylinderRegion;
import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.RandomCollection;
import de.hglabor.utils.noriskutils.TimeConverter;
import de.hglabor.utils.noriskutils.WorldEditUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Feast implements Listener {
    public static final String BLOCK_KEY = "FEAST_BLOCK";
    private static final Random random = new Random();

    private final Set<Block> feastBlocks;
    private final JavaPlugin plugin;
    private final World world;
    private final BossBar feastBossBar;
    private Location feastCenter;
    private Material platformMaterial;
    private int radius, timer, totalTime, airHeight, maxItemsInChest;
    private boolean inPreparation, isFinished, shouldDamageItems;

    public Feast(JavaPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
        this.feastBlocks = new HashSet<>();
        this.maxItemsInChest = 6;
        this.feastBossBar = Bukkit.createBossBar("Feast", BarColor.GREEN, BarStyle.SOLID);
    }

    public Feast center(Location feastCenter) {
        this.feastCenter = feastCenter;
        return this;
    }

    public Feast material(Material platformMaterial) {
        this.platformMaterial = platformMaterial;
        return this;
    }

    public Feast radius(int radius) {
        this.radius = radius;
        return this;
    }

    public Feast timer(int timer) {
        this.timer = timer;
        this.totalTime = timer;
        return this;
    }

    public Feast maxItemsInChest(int maxItemsInChest) {
        this.maxItemsInChest = maxItemsInChest;
        return this;
    }

    public Feast air(int height) {
        this.airHeight = height;
        return this;
    }

    public Feast damageItems(boolean shouldDamage) {
        this.shouldDamageItems = shouldDamage;
        return this;
    }

    public void spawn() {
        inPreparation = true;

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 1, 1));

        WorldEditUtils.createCylinder(world, feastCenter, radius, true, 1, platformMaterial);
        WorldEditUtils.createCylinder(world, feastCenter.clone().add(0, 1, 0), radius, true, airHeight, Material.AIR);

        CylinderRegion cylinderRegion = new CylinderRegion(BukkitAdapter.adapt(world),
                BukkitAdapter.asBlockVector(feastCenter), Vector2.at(radius, radius),
                feastCenter.getBlockY(), feastCenter.getBlockY() + airHeight);

        for (BlockVector3 blockVector3 : cylinderRegion) {
            feastBlocks.add(world.getBlockAt(BukkitAdapter.adapt(world, blockVector3)));
        }

        this.feastBlocks.forEach(feastBlock -> feastBlock.setMetadata(BLOCK_KEY, new FixedMetadataValue(plugin, "")));
        this.startCountDown();
    }

    private void spawnFeastLoot() {
        feastCenter.clone().add(0, 1, 0).getBlock().setType(Material.ENCHANTING_TABLE);

        Location[] chestLocations = new Location[12];

        chestLocations[0] = feastCenter.clone().add(1, 1, 1);
        chestLocations[1] = feastCenter.clone().add(-1, 1, 1);
        chestLocations[2] = feastCenter.clone().add(-1, 1, -1);
        chestLocations[3] = feastCenter.clone().add(1, 1, -1);
        chestLocations[4] = feastCenter.clone().add(2, 1, 2);
        chestLocations[5] = feastCenter.clone().add(0, 1, 2);
        chestLocations[6] = feastCenter.clone().add(-2, 1, 2);
        chestLocations[7] = feastCenter.clone().add(2, 1, 0);
        chestLocations[8] = feastCenter.clone().add(-2, 1, 0);
        chestLocations[9] = feastCenter.clone().add(2, 1, -2);
        chestLocations[10] = feastCenter.clone().add(0, 1, -2);
        chestLocations[11] = feastCenter.clone().add(-2, 1, -2);

        Arrays.stream(chestLocations).forEach(chestLocation -> chestLocation.getBlock().setType(Material.CHEST));

        //FEAST ITEMS
        RandomCollection<ItemStack> diamondItems = new RandomCollection<>();
        diamondItems.add(1, new ItemStack(Material.DIAMOND_HELMET));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_CHESTPLATE));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_LEGGINGS));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_BOOTS));
        diamondItems.add(1, new ItemStack(Material.DIAMOND_SWORD));

        RandomCollection<ItemStack> netheriteItems = new RandomCollection<>();
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_HELMET));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_CHESTPLATE));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_LEGGINGS));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_BOOTS));
        netheriteItems.add(1, new ItemStack(Material.NETHERITE_SWORD));

        RandomCollection<ItemStack> sizeableItems = new RandomCollection<>();
        sizeableItems.add(1, new ItemStack(Material.SPECTRAL_ARROW));
        sizeableItems.add(1, new ItemStack(Material.LAPIS_LAZULI));
        sizeableItems.add(1, new ItemStack(Material.COOKED_BEEF));
        sizeableItems.add(1, new ItemStack(Material.COOKED_PORKCHOP));
        sizeableItems.add(1, new ItemStack(Material.COOKED_CHICKEN));
        sizeableItems.add(1, new ItemStack(Material.MUSHROOM_STEW));

        RandomCollection<ItemStack> singleItems = new RandomCollection<>();
        singleItems.add(1, new ItemStack(Material.BOW));
        singleItems.add(1, new ItemStack(Material.COBWEB));
        singleItems.add(1, new ItemStack(Material.FLINT_AND_STEEL));
        singleItems.add(1, new ItemStack(Material.TNT));
        singleItems.add(1, new ItemStack(Material.ENDER_PEARL));
        singleItems.add(1, new ItemStack(Material.LAVA_BUCKET));
        singleItems.add(1, new ItemStack(Material.WATER_BUCKET));

        RandomCollection<RandomCollection<ItemStack>> lootPool = new RandomCollection<>();
        lootPool.add(20, diamondItems);
        lootPool.add(33, sizeableItems);
        lootPool.add(33, singleItems);
        lootPool.add(3, netheriteItems);


        for (Location chestLocation : chestLocations) {
            Chest chest = (Chest) chestLocation.getBlock().getState();
            for (int i = 0; i < maxItemsInChest; i++) {
                RandomCollection<ItemStack> randomItemCollection = lootPool.getRandom();
                ItemStack item = randomItemCollection.getRandom();

                if (randomItemCollection.equals(sizeableItems)) {
                    item.setAmount(random.nextInt(10) + 1);
                }

                if (shouldDamageItems) {
                    if (randomItemCollection.equals(netheriteItems) || randomItemCollection.equals(diamondItems)) {
                        Damageable damageable = (Damageable) item.getItemMeta();
                        int maxDurability = item.getType().getMaxDurability();
                        damageable.setDamage(maxDurability - random.nextInt(maxDurability / 4));
                        item.setItemMeta((ItemMeta) damageable);
                    }
                }


                chest.getInventory().setItem(random.nextInt(26 - 1) + 1, item);
            }
        }
    }

    private void startCountDown() {
        new BukkitRunnable() {
            @Override
            public void run() {

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (!feastBossBar.getPlayers().contains(player)) {
                        feastBossBar.addPlayer(player);
                    }
                });

                feastBossBar.setTitle(Localization.INSTANCE.getMessage("feast.broadcastMessage", ImmutableMap.of("centerLocation", printFeastCenter(), "timeString", TimeConverter.stringify(timer)), Locale.ENGLISH));
                feastBossBar.setProgress((double) timer / totalTime);

                if (timer % 60 == 0 || timer < 10) {
                    ChatUtils.broadcastMessage("feast.broadcastMessage", ImmutableMap.of(
                            "centerLocation", printFeastCenter(),
                            "timeString", TimeConverter.stringify(timer)));
                }

                timer--;

                if (timer <= 0) {
                    //CHEST SPAWNING
                    inPreparation = false;
                    isFinished = true;
                    feastBossBar.removeAll();
                    feastBlocks.forEach(feastBlock -> feastBlock.removeMetadata(BLOCK_KEY, plugin));
                    spawnFeastLoot();
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public String printFeastCenter() {
        return "[" + (int) feastCenter.getX() + ", " + (int) feastCenter.getY() + ", " + (int) feastCenter.getZ() + "]";
    }
}

