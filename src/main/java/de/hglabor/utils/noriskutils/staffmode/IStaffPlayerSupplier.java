package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface IStaffPlayerSupplier {
    Optional<IStaffPlayer> getStaffPlayer(Player player);

    Optional<Player> getRandomActivePlayer();
}
