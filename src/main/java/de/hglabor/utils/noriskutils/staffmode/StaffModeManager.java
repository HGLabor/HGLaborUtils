package de.hglabor.utils.noriskutils.staffmode;

import com.google.common.collect.ImmutableList;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class StaffModeManager {
    public final static StaffModeManager INSTANCE = new StaffModeManager();
    protected PlayerHider playerHider;
    protected final ItemStack INVENTORY_VIEWER;
    protected final ItemStack RANDOM_TELEPORT;
    protected final ItemStack PLAYER_STATS;
    protected final ItemStack TOGGLE_VISIBILITY;
    protected final ItemStack TOGGLE_BUILD_MODE;

    private StaffModeManager() {
        this.INVENTORY_VIEWER = new ItemBuilder(Material.BLAZE_ROD).setName("Inventory Viewer").build();
        this.RANDOM_TELEPORT = new ItemBuilder(Material.PLAYER_HEAD).setName("Random Teleport").build();
        this.PLAYER_STATS = new ItemBuilder(Material.REDSTONE_BLOCK).setName("Player Stats").build();
        this.TOGGLE_VISIBILITY = new ItemBuilder(Material.NETHER_STAR).setName("Toggle Visibility").build();
        this.TOGGLE_BUILD_MODE = new ItemBuilder(Material.BRICK).setName("Toggle Build Mode").build();
    }

    public List<ItemStack> getStaffModeItems() {
        return ImmutableList.of(RANDOM_TELEPORT, INVENTORY_VIEWER, PLAYER_STATS, TOGGLE_VISIBILITY, TOGGLE_BUILD_MODE);
    }

    public PlayerHider getPlayerHider() {
        return playerHider;
    }

    public void setPlayerHider(PlayerHider playerHider) {
        this.playerHider = playerHider;
    }
}
