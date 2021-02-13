package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerHider implements Listener {
    private final JavaPlugin plugin;
    private final StaffPlayerSupplier supplier;

    public PlayerHider(StaffPlayerSupplier supplier, JavaPlugin plugin) {
        this.supplier = supplier;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void hide(Player playerToHide) {
        supplier.getStaffPlayer(playerToHide).setVisible(false);
        for (Player player : Bukkit.getOnlinePlayers()) {
            StaffPlayer staffPlayer = supplier.getStaffPlayer(player);
            if (player.hasPermission("hglabor.staffmode")) {
                if (staffPlayer.canSeeStaffModePlayers()) continue;
                if (staffPlayer.isStaffMode()) continue;
            }
            player.hidePlayer(plugin, playerToHide);
        }
    }

    public void show(Player playerToShow) {
        supplier.getStaffPlayer(playerToShow).setVisible(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, playerToShow);
        }
    }

    public void hideEveryoneInStaffMode(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            StaffPlayer staffPlayer = supplier.getStaffPlayer(otherPlayer);
            if (staffPlayer.isStaffMode()) {
                player.hidePlayer(plugin, otherPlayer);
            }
        }
    }

    public void showEveryoneInStaffMode(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            StaffPlayer staffPlayer = supplier.getStaffPlayer(otherPlayer);
            if (staffPlayer.isStaffMode()) {
                player.showPlayer(plugin, otherPlayer);
            }
        }
    }

    public StaffPlayerSupplier getSupplier() {
        return supplier;
    }
}
