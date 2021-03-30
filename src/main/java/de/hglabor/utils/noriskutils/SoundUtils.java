package de.hglabor.utils.noriskutils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundUtils {
    private SoundUtils() {

    }

    public static void playTeleportSound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 10);
    }
}
