package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;

public interface IStaffPlayerSupplier {
    IStaffPlayer getStaffPlayer(Player player);

    Player getRandomActivePlayer();
}
