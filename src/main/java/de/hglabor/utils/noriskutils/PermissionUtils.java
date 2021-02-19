package de.hglabor.utils.noriskutils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PermissionUtils {
    private static final List<String> groups = List.of("admin", "coder", "mod+", "moderator", "media", "creativity", "default");
    private static final Map<String, Integer> groupWeight;

    static {
        groupWeight = new HashMap<>();
        groupWeight.put("admin", 1000);
        groupWeight.put("coder", 900);
        groupWeight.put("mod+", 800);
        groupWeight.put("moderator", 700);
        groupWeight.put("media", 600);
        groupWeight.put("creativity", 500);
        groupWeight.put("default", 0);
    }

    private PermissionUtils() {
    }

    public static boolean checkForHigherRank(Player player) {
        String playerGroup = getPlayerGroup(player, groups);
        if ("admin".equalsIgnoreCase(playerGroup)) {
            return false;
        }
        int weight = groupWeight.getOrDefault(playerGroup, 0);
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            String group = getPlayerGroup(otherPlayer, groups);
            if (groupWeight.getOrDefault(group, 0) > weight) {
                return true;
            }
        }
        return false;
    }

    public static String getPlayerGroup(Player player) {
        return getPlayerGroup(player, groups);
    }

    public static String getPlayerGroup(Player player, Collection<String> possibleGroups) {
        for (String group : possibleGroups) {
            if (player.hasPermission("group." + group)) {
                return group;
            }
        }
        return "default";
    }
}
