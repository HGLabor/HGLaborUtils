package de.hglabor.utils.noriskutils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class PotionUtils {
    private PotionUtils() {
    }

    public static void paralysePlayer(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1000000));
    }

    public static void removePotionEffects(Player player) {
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
    }
}
