package de.hglabor.utils.noriskutils.listener;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RemoveHitCooldown implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        disableCooldown(e.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        disableCooldown(e.getPlayer());
    }

    private void disableCooldown(Player p) {
        AttributeInstance attackSpeedAttribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attackSpeedAttribute != null) {
            attackSpeedAttribute.setBaseValue(100);
        }
    }
}
