package de.hglabor.utils.noriskutils.staffmode;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class StaffModeCommand {
    public StaffModeCommand(IStaffPlayerSupplier staffPlayerSupplier, String permission) {
        new CommandAPICommand("staffmode")
                .withPermission(permission)
                .withArguments()
                .withRequirement(commandSender -> commandSender instanceof Player)
                .executesPlayer((player, objects) -> {
                    IStaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
                    staffPlayer.toggleStaffMode();
                })
                .register();
    }
}
