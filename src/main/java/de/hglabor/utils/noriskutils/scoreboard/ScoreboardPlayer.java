package de.hglabor.utils.noriskutils.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Locale;

public interface ScoreboardPlayer {
    Scoreboard getScoreboard();

    void setScoreboard(Scoreboard scoreboard);

    Objective getObjective();

    void setObjective(Objective objective);

    Locale getLocale();

    Player getPlayer();
}
