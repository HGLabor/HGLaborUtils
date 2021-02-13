package de.hglabor.utils.noriskutils;

import de.hglabor.utils.localization.Localization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class ChatUtils {
    private ChatUtils() {
    }

    public static void broadcastMessage(String key, Map<String, String> values) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Localization.INSTANCE.getMessage(key, values, getPlayerLocale(player.getUniqueId()))));
    }

    public static void broadcastMessage(String key) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Localization.INSTANCE.getMessage(key, getPlayerLocale(player.getUniqueId()))));
    }

    public static Locale getPlayerLocale(Player player) {
        return getPlayerLocale(player.getUniqueId());
    }

    public static Locale getPlayerLocale(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Locale playerLocale = new Locale(player.getLocale());

            if (playerLocale.getLanguage().startsWith("de")) {
                return Locale.GERMAN;
            }

            if (playerLocale.getLanguage().startsWith("en")) {
                return Locale.ENGLISH;
            }

        }
        return Locale.ENGLISH;
    }

}
