package de.hglabor.utils.noriskutils.staffmode;

import org.bukkit.entity.Player;

public interface IStaffPlayer {
    boolean isStaffMode();

    void toggleStaffMode();

    void printStatsOf(Player player);

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isBuildMode();

    void setBuildMode(boolean value);

    void setCanSeeStaffModePlayers(boolean value);

    boolean canSeeStaffModePlayers();
}
