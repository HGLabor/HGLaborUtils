package de.hglabor.utils.noriskutils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public final class HologramUtils {
    private HologramUtils() {
    }

    public static ArmorStand spawnHologram(Location location, String text) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setCanMove(false);
        armorStand.setMarker(true);
        armorStand.setCustomName(text);
        armorStand.setCustomNameVisible(true);
        return armorStand;
    }
}
