package de.hglabor.utils.noriskutils.listener;

import com.google.gson.JsonObject;
import de.hglabor.utils.noriskutils.ClientPayloadBuffer;
import de.hglabor.utils.noriskutils.NMSUtils;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for LabyMods new combat system
 * in the 1.16.5 for HGLabor.de
 *
 * Use `new LabyModOldPvP(plugin);` to register listening
 *
 * @author Raik
 * @version 1.0
 */
public class LabyModOldPvP implements PluginMessageListener {

    /**
     * Channel name used by LabyMod for communication
     */
    private static final String CHANNEL_NAME = "labymod3:main";

    /**
     * Constructor setting up the the listening
     *
     * @param plugin The plugin instance
     */
    public LabyModOldPvP(JavaPlugin plugin) {
        //Register listening
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, CHANNEL_NAME, this);
    }


    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channel.
     * Catching labymods join message to send perm message
     *
     * @param channel Channel that the message was sent through.
     * @param player  Source of the message.
     * @param message The raw message that was sent.
     */
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals(CHANNEL_NAME)) {
            return;
        }

        ClientPayloadBuffer payloadBuffer = new ClientPayloadBuffer(message);
        //Only execute on join / INFO message
        if (payloadBuffer.readString().equals("INFO")) {
            this.sendPermissions(player);
        }
    }

    /**
     * Send the permission to the player
     * using plugin channels
     *
     * @param receiver The receiver of the permissions
     */
    private void sendPermissions(Player receiver) {
        ClientPayloadBuffer payloadBuffer = new ClientPayloadBuffer();
        //Set permission key
        payloadBuffer.writeString("PERMISSIONS");
        //Set permission
        payloadBuffer.writeString(LabyModSettings.getSettingsAsJsonString());

        //Creating packet
        PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload(new MinecraftKey(CHANNEL_NAME)
                , new PacketDataSerializer(payloadBuffer.getByteBuf()));

        //Sending packet
        NMSUtils.sendPacket(receiver, payloadPacket);
    }

    /**
     * Enum containing every changed
     * settings which will be transmitted to the player
     */
    private enum LabyModSettings {
        IMPROVED_LAVA(true), //Minecraft bug fix provided by LabyMod
        RANGE(true), //Adding 1.8 Range to 1.16
        SLOWDOWN(true); //Adding slowdown on blocking

        /**
         * The state whether a features is enabled or not
         */
        private final boolean state;

        LabyModSettings(boolean state) {
            this.state = state;
        }

        /**
         * Getting all Settings as Json String
         * using JsonObject and looping through
         * als set values
         *
         * @return The json srting
         */
        public static String getSettingsAsJsonString() {
            JsonObject settings = new JsonObject();

            //Looping through settings to add them
            for (LabyModSettings setting: values()) {
                settings.addProperty(setting.name(), setting.state);
            }

            return settings.toString();
        }
    }
}
