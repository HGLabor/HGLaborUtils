package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StaffModeListener implements Listener {
    private final StaffPlayerSupplier staffPlayerSupplier;

    public StaffModeListener(StaffPlayerSupplier staffPlayerSupplier) {
        this.staffPlayerSupplier = staffPlayerSupplier;
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        StaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        ItemStack item = player.getInventory().getItem(event.getHand());
        if (staffPlayer.isStaffMode() && event.getRightClicked() instanceof Player && item != null) {
            Player rightClicked = (Player) event.getRightClicked();
            if (item.isSimilar(StaffModeManager.INSTANCE.PLAYER_STATS)) {
                staffPlayer.printStatsOf(rightClicked);
            } else if (item.isSimilar(StaffModeManager.INSTANCE.INVENTORY_VIEWER)) {
                player.openInventory(rightClicked.getInventory());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.hasItem()) {
            ItemStack item = event.getItem();
            Player player = event.getPlayer();
            StaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
            if (staffPlayer.isStaffMode() && item != null) {
                if (item.isSimilar(StaffModeManager.INSTANCE.RANDOM_TELEPORT)) {
                    player.teleport(staffPlayerSupplier.getRandomActivePlayer());
                } else if (item.isSimilar(StaffModeManager.INSTANCE.TOGGLE_VISIBILITY)) {
                    if (staffPlayer.isVisible()) {
                        StaffModeManager.INSTANCE.getPlayerHider().hide(player);
                    } else {
                        StaffModeManager.INSTANCE.getPlayerHider().show(player);
                    }
                }
            }
        }
    }
}
