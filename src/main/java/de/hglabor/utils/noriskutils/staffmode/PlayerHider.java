package de.hglabor.utils.noriskutils.staffmode;

import de.hglabor.utils.localization.Localization;
import de.hglabor.utils.noriskutils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class PlayerHider implements Listener {
    private final JavaPlugin plugin;
    private final IStaffPlayerSupplier supplier;

    public PlayerHider(IStaffPlayerSupplier supplier, JavaPlugin plugin) {
        this.supplier = supplier;
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        //haha joint
        Player joined = event.getPlayer();
        supplier.getStaffPlayer(joined).ifPresent(iStaffPlayer -> {
            if (iStaffPlayer.canSeeStaffModePlayers()) {
                return;
            }
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                supplier.getStaffPlayer(otherPlayer).ifPresent(otherStaffPlayer -> {
                    if (otherStaffPlayer.isStaffMode() && !otherStaffPlayer.isVisible()) {
                        joined.hidePlayer(plugin, otherPlayer);
                    }
                });
            }
        });
    }

    public void hide(Player playerToHide) {
        supplier.getStaffPlayer(playerToHide).ifPresent(iStaffPlayer -> {
            iStaffPlayer.setVisible(false);
            playerToHide.sendActionBar(Localization.INSTANCE.getMessage("staffmode.hidden", ChatUtils.locale(playerToHide)));
            for (Player player : Bukkit.getOnlinePlayers()) {
                Optional<IStaffPlayer> staffPlayer = supplier.getStaffPlayer(player);
                if (player.hasPermission("hglabor.staffmode")) {
                    if (staffPlayer.isEmpty()) continue;
                    if (staffPlayer.get().canSeeStaffModePlayers()) continue;
                    if (staffPlayer.get().isStaffMode()) continue;
                }
                player.hidePlayer(plugin, playerToHide);
            }
        });
    }

    public void show(Player playerToShow) {
        supplier.getStaffPlayer(playerToShow).ifPresent(iStaffPlayer -> iStaffPlayer.setVisible(true));
        playerToShow.sendActionBar(Localization.INSTANCE.getMessage("staffmode.visible", ChatUtils.locale(playerToShow)));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, playerToShow);
        }
    }

    public void hideEveryoneInStaffMode(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            supplier.getStaffPlayer(otherPlayer).ifPresent(iStaffPlayer -> {
                if (iStaffPlayer.isStaffMode()) {
                    player.hidePlayer(plugin, otherPlayer);
                }
            });
        }
    }

    public void showEveryoneInStaffMode(Player player) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            supplier.getStaffPlayer(otherPlayer).ifPresent(iStaffPlayer -> {
                if (iStaffPlayer.isStaffMode()) {
                    player.showPlayer(plugin, otherPlayer);
                }
            });
        }
    }

    public IStaffPlayerSupplier getSupplier() {
        return supplier;
    }
}
