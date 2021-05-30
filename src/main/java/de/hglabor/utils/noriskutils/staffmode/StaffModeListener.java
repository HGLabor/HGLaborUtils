package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StaffModeListener implements Listener {
    private final IStaffPlayerSupplier staffPlayerSupplier;

    public StaffModeListener(IStaffPlayerSupplier staffPlayerSupplier) {
        this.staffPlayerSupplier = staffPlayerSupplier;
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        ItemStack item = player.getInventory().getItem(event.getHand());

        if (!staffPlayer.isStaffMode()) {
            return;
        }
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        if (item == null) {
            return;
        }

        Player rightClicked = (Player) event.getRightClicked();
        if (item.isSimilar(StaffModeManager.INSTANCE.PLAYER_STATS)) {
            staffPlayer.printStatsOf(rightClicked);
        } else if (item.isSimilar(StaffModeManager.INSTANCE.INVENTORY_VIEWER)) {
            player.openInventory(rightClicked.getInventory());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        if (!staffPlayer.isStaffMode()) {
            return;
        }
        if (!staffPlayer.isBuildMode()) {
            event.setCancelled(true);
        } else {
            player.sendMessage(ChatColor.RED + "You can only break blocks in build mode");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        if (!staffPlayer.isStaffMode()) {
            return;
        }
        if (!staffPlayer.isBuildMode()) {
            event.setCancelled(true);
        } else {
            player.sendMessage(ChatColor.RED + "You can only place blocks in build mode");
        }
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        if (!staffPlayer.isStaffMode()) {
            return;
        }
        if (!staffPlayer.isBuildMode()) {
            event.setCancelled(true);
        } else {
            player.sendMessage(ChatColor.RED + "You can only pickup items in build mode");
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        if (!staffPlayer.isStaffMode() || item == null) {
            return;
        }

        if (item.isSimilar(StaffModeManager.INSTANCE.RANDOM_TELEPORT)) {
            player.teleport(staffPlayerSupplier.getRandomActivePlayer());
            return;
        }

        if (item.isSimilar(StaffModeManager.INSTANCE.TOGGLE_VISIBILITY)) {
            if (staffPlayer.isVisible()) {
                StaffModeManager.INSTANCE.getPlayerHider().hide(player);
            } else {
                StaffModeManager.INSTANCE.getPlayerHider().show(player);
            }
            return;
        }

        if (item.isSimilar(StaffModeManager.INSTANCE.TOGGLE_BUILD_MODE)) {
            staffPlayer.setBuildMode(!staffPlayer.isBuildMode());
            return;
        }
    }
}
