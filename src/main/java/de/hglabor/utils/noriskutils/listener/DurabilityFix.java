package de.hglabor.utils.noriskutils.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DurabilityFix implements Listener {
    private final static Random random = new Random();

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        if (item.getType().name().endsWith("_SWORD") || item.getType().name().endsWith("_AXE")) {
            if (random.nextBoolean()) e.setCancelled(true);
        }
    }
}
