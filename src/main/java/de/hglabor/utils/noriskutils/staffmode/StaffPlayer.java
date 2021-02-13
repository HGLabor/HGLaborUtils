package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;

public interface StaffPlayer {
    boolean isStaffMode();

    void toggleStaffMode();

    void printStatsOf(Player player);

    boolean isVisible();

    void setVisible(boolean visible);

    void setCanSeeStaffModePlayers(boolean value);

    boolean canSeeStaffModePlayers();
}
