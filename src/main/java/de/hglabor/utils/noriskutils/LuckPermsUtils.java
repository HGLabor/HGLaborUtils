package de.hglabor.utils.noriskutils;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.stream.Collectors;

public final class LuckPermsUtils {
    private LuckPermsUtils() {
    }

    public static Group getPlayerGroup(Player player) {
        for (Group group : LuckPermsProvider.get().getGroupManager().getLoadedGroups().stream().sorted(Comparator.comparingInt(value -> ((Group) value).getWeight().orElse(0)).reversed()).collect(Collectors.toList())) {
            if (player.hasPermission("group." + group.getName())) {
                return group;
            }
        }
        return LuckPermsProvider.get().getGroupManager().getGroup("default");
    }

    public static String getGroupNameColor(Group group) {
        String hexColor = group.getCachedData().getMetaData().getMetaValue("name_color");
        return hexColor != null ? hexColor : "#8d8d8d";
    }

    public static String getGroupNameColor(Player player) {
        return getGroupNameColor(getPlayerGroup(player));
    }
}
