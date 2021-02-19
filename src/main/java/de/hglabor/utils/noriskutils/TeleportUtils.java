package de.hglabor.utils.noriskutils;

import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class TeleportUtils {
    private TeleportUtils() {
    }

    public static void toSafeSpawnLocation(Player player, int maxBound, int minBound) {
        toSafeSpawnLocation(player.getWorld().getSpawnLocation(), player, maxBound, minBound);
    }

    public static void toSafeSpawnLocation(Location center, Player player, int maxBound, int minBound) {
        player.teleportAsync(getHighestRandomLocation(center.getWorld(), maxBound, minBound));
    }

    public static Location getHighestRandomLocation(World world, int maxBound, int minBound) {
        return getHighestRandomLocation(world, maxBound, minBound, HeightMap.MOTION_BLOCKING_NO_LEAVES);
    }

    public static Location getHighestRandomLocation(World world, int maxBound, int minBound, HeightMap heightMap) {
        int x = ChanceUtils.getRandomNumber(maxBound, minBound);
        int z = ChanceUtils.getRandomNumber(maxBound, minBound);
        return world.getHighestBlockAt(x, z, heightMap).getLocation().clone().add(0, 1, 0);
    }
}
