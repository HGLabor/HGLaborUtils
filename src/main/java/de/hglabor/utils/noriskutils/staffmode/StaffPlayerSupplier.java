package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;

public interface StaffPlayerSupplier {
    StaffPlayer getStaffPlayer(Player player);

    Player getRandomActivePlayer();
}
