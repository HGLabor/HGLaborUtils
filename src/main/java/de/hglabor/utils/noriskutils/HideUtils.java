package de.hglabor.utils.noriskutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class HideUtils implements Listener {
    public final static HideUtils INSTANCE = new HideUtils();
    private final Set<UUID> disabled;
    private final Set<UUID> hiddenPlayers;
    private JavaPlugin plugin;

    private HideUtils() {
        this.disabled = new HashSet<>();
        this.hiddenPlayers = new HashSet<>();
    }

    public void hide(Player playerToHide) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("hglabor.staffmode")) {
                if (!disabled.contains(player.getUniqueId())) {
                    player.hidePlayer(plugin, playerToHide);
                }
            } else {
                player.hidePlayer(plugin, playerToHide);
            }
        }
    }

    public void put(Player player) {
        disabled.add(player.getUniqueId());
    }

    public void remove(Player player) {
        disabled.remove(player.getUniqueId());
    }

    public void register(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
