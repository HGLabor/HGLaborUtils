package de.hglabor.utils.noriskutils.pvpbots;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPBotAnimationListener implements Listener {
    private final JavaPlugin plugin;
    private final long delay;

    public PvPBotAnimationListener(JavaPlugin plugin, long delay) {
        this.plugin = plugin;
        this.delay = delay;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.getEntity().hasMetadata(PvPBot.META_DATA)) {
            return;
        }
        Zombie zombie = (Zombie) event.getEntity();
        if (zombie.getEquipment() == null) {
            return;
        }
        final ItemStack oldItem = zombie.getEquipment().getItemInMainHand().clone();
        zombie.getEquipment().setItemInMainHand(new ItemStack(Material.MUSHROOM_STEW));
        Bukkit.getScheduler().runTaskLater(plugin, () -> zombie.getEquipment().setItemInMainHand(oldItem), delay);
    }
}
