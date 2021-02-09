package de.hglabor.utils.noriskutils.feast;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FeastListener implements Listener {

    @EventHandler
    public void onBreakFeastBlock(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata(Feast.BLOCK_KEY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceFeastBlock(BlockPlaceEvent event) {
        if (event.getBlockAgainst().hasMetadata(Feast.BLOCK_KEY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosionFeastEvent(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if (block.hasMetadata(Feast.BLOCK_KEY)) {
                event.setCancelled(true);
            }
        }
    }
}
