package de.hglabor.utils.noriskutils.scoreboard;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

public final class ScoreboardFactory {
    private ScoreboardFactory() {
    }

    public static void create(ScoreboardPlayer scoreboardPlayer, String title, boolean isCollision) {
        if (scoreboardPlayer.getScoreboard() == null && scoreboardPlayer.getObjective() == null) {
            scoreboardPlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            if (isCollision) {
                Team collision = scoreboardPlayer.getScoreboard().registerNewTeam("collision");
                collision.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }
            scoreboardPlayer.setObjective(scoreboardPlayer.getScoreboard().registerNewObjective("aaa", "dummy", title));
            scoreboardPlayer.getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        Player player = scoreboardPlayer.getPlayer();
        player.setScoreboard(scoreboardPlayer.getScoreboard());
    }

    public static void addEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix, int score) {
        Team team = scoreboardPlayer.getScoreboard().registerNewTeam(name);
        team.addEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
        scoreboardPlayer.getObjective().getScore(ChatColor.values()[score] + "" + ChatColor.WHITE).setScore(score);
    }

    public static void addEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, int score) {
        addEntry(scoreboardPlayer, name, prefix, "", score);
    }

    public static void updateEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix, String suffix) {
        if (scoreboardPlayer != null && scoreboardPlayer.getScoreboard() != null) {
            Team team = scoreboardPlayer.getScoreboard().getTeam(name);
            if (team != null) {
                team.setPrefix(prefix);
                team.setSuffix(suffix);
            }
        }
    }

    public static void updateEntry(ScoreboardPlayer scoreboardPlayer, String name, String prefix) {
        updateEntry(scoreboardPlayer, name, prefix, "");
    }

    public static void removeEntry(ScoreboardPlayer scoreboardPlayer, String name, int score) {
        Team team = scoreboardPlayer.getScoreboard().getTeam(name);
        if (team != null) {
            team.removeEntry(ChatColor.values()[score] + "" + ChatColor.WHITE);
        }
    }

    public static void addPlayerToNoCollision(Player playerToAdd, ScoreboardPlayer scoreboardPlayer) {
        Team collision = scoreboardPlayer.getScoreboard().getTeam("collision");
        if (collision != null && !collision.hasEntry(playerToAdd.getName())) {
            collision.addEntry(playerToAdd.getName());
        }
    }
}

