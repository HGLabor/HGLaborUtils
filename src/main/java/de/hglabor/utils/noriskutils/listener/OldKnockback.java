package de.hglabor.utils.noriskutils.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class OldKnockback implements Listener {
    private final JavaPlugin plugin;

    public OldKnockback(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        activateOldKnockback(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        activateOldKnockback(e.getPlayer());
    }

    private void activateOldKnockback(Player p) {
        p.setMetadata("oldKnockback", new FixedMetadataValue(plugin, ""));
    }
}
