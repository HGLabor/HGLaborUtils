package de.hglabor.utils.noriskutils.staffmode;

import com.google.common.collect.ImmutableList;
import de.hglabor.utils.noriskutils.ItemBuilder;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

public final class StaffModeManager implements Listener {
    public final static StaffModeManager INSTANCE = new StaffModeManager();
    protected final ItemStack INVENTORY_VIEWER;
    protected final ItemStack RANDOM_TELEPORT;
    protected final ItemStack PLAYER_STATS;
    protected final ItemStack TOGGLE_VISIBILITY;
    protected final ItemStack TOGGLE_BUILD_MODE;
    protected PlayerHider playerHider;
    protected JavaPlugin plugin;
    protected IStaffPlayerSupplier staffPlayerSupplier;

    private StaffModeManager() {
        this.INVENTORY_VIEWER = new ItemBuilder(Material.BLAZE_ROD).setName("Inventory Viewer").build();
        this.RANDOM_TELEPORT = new ItemBuilder(Material.PLAYER_HEAD).setName("Random Teleport").build();
        this.PLAYER_STATS = new ItemBuilder(Material.REDSTONE_BLOCK).setName("Player Stats").build();
        this.TOGGLE_VISIBILITY = new ItemBuilder(Material.NETHER_STAR).setName("Toggle Visibility").build();
        this.TOGGLE_BUILD_MODE = new ItemBuilder(Material.BRICK).setName("Toggle Build Mode").build();
    }

    public void onEnable(JavaPlugin plugin, IStaffPlayerSupplier staffPlayerSupplier, String staffModePermission) {
        if (this.plugin != null) {
            return;
        }
        this.plugin = plugin;
        this.staffPlayerSupplier = staffPlayerSupplier;
        this.playerHider = new PlayerHider(staffPlayerSupplier, plugin);
        new CommandAPICommand("staffmode")
                .withPermission(staffModePermission)
                .withRequirement(commandSender -> commandSender instanceof Player)
                .executesPlayer((player, objects) -> {
                    staffPlayerSupplier.getStaffPlayer(player).ifPresent(IStaffPlayer::toggleStaffMode);
                }).register();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getPluginManager().registerEvents(this.playerHider, plugin);
    }

    public PlayerHider getPlayerHider() {
        return playerHider;
    }

    public void sendStaffModeInformation() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            staffPlayerSupplier.getStaffPlayer(player).ifPresent(staffPlayer -> {
                if (!staffPlayer.isStaffMode()) return;
                String buildInfo = staffPlayer.isBuildMode() ? ChatColor.GREEN + "Buildmode" : ChatColor.RED + "No Buildmode";
                String visibilityInfo = staffPlayer.isVisible() ? ChatColor.GREEN + "Visible" : ChatColor.RED + "Hidden";
                player.sendActionBar(visibilityInfo + " - " + buildInfo);
            });
        }
    }

    public List<ItemStack> getStaffModeItems() {
        return ImmutableList.of(RANDOM_TELEPORT, INVENTORY_VIEWER, PLAYER_STATS, TOGGLE_VISIBILITY, TOGGLE_BUILD_MODE);
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        staffPlayerSupplier.getStaffPlayer(player).ifPresent(iStaffPlayer -> {
            ItemStack item = player.getInventory().getItem(event.getHand());

            if (!iStaffPlayer.isStaffMode()) {
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
                iStaffPlayer.printStatsOf(rightClicked);
            } else if (item.isSimilar(StaffModeManager.INSTANCE.INVENTORY_VIEWER)) {
                player.openInventory(rightClicked.getInventory());
            }
        });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        staffPlayerSupplier.getStaffPlayer(player).ifPresent(iStaffPlayer -> {
            if (!iStaffPlayer.isStaffMode()) {
                return;
            }
            if (!iStaffPlayer.isBuildMode()) {
                event.setCancelled(true);
            } else {
                player.sendMessage(ChatColor.RED + "You can only break blocks in build mode");
            }
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        staffPlayerSupplier.getStaffPlayer(player).ifPresent(iStaffPlayer -> {
            if (!iStaffPlayer.isStaffMode()) {
                return;
            }
            if (!iStaffPlayer.isBuildMode()) {
                event.setCancelled(true);
            } else {
                player.sendMessage(ChatColor.RED + "You can only place blocks in build mode");
            }
        });
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        staffPlayerSupplier.getStaffPlayer(player).ifPresent(iStaffPlayer -> {
            if (!iStaffPlayer.isStaffMode()) {
                return;
            }
            if (!iStaffPlayer.isBuildMode()) {
                event.setCancelled(true);
            } else {
                player.sendMessage(ChatColor.RED + "You can only pickup items in build mode");
            }
        });
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        Optional<IStaffPlayer> optionalIStaffPlayer = staffPlayerSupplier.getStaffPlayer(player);
        if (optionalIStaffPlayer.isEmpty()) {
            return;
        }
        IStaffPlayer staffPlayer = optionalIStaffPlayer.get();
        if (!staffPlayer.isStaffMode() || item == null) {
            return;
        }
        if (item.isSimilar(StaffModeManager.INSTANCE.RANDOM_TELEPORT)) {
            staffPlayerSupplier.getRandomActivePlayer().ifPresent(player::teleport);
            return;
        }

        if (item.isSimilar(StaffModeManager.INSTANCE.TOGGLE_VISIBILITY)) {
            if (staffPlayer.isVisible()) {
                playerHider.hide(player);
            } else {
                playerHider.show(player);
            }
            return;
        }

        if (item.isSimilar(StaffModeManager.INSTANCE.TOGGLE_BUILD_MODE)) {
            staffPlayer.setBuildMode(!staffPlayer.isBuildMode());
            return;
        }
    }
}
