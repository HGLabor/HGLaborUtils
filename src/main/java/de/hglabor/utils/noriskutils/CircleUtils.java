package de.hglabor.utils.noriskutils;

import org.bukkit.Location;

import java.util.HashSet;

public final class CircleUtils {
    private CircleUtils() {
    }

    public static HashSet<Location> makeCircle(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        HashSet<Location> surroundingBlocks = new HashSet<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        surroundingBlocks.add(loc.getWorld().getBlockAt(x, y + plus_y, z).getLocation());
                    }
                }
            }
        }
        return surroundingBlocks;
    }
}
