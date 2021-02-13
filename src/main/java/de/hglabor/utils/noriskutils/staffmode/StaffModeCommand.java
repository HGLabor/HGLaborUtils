package de.hglabor.utils.noriskutils.staffmode;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Player;

public class StaffModeCommand {
    private final StaffPlayerSupplier staffPlayerSupplier;

    public StaffModeCommand(StaffPlayerSupplier staffPlayerSupplier) {
        this.staffPlayerSupplier = staffPlayerSupplier;
        new CommandAPICommand("staffmode")
                .withPermission("hglabor.staffmode")
                .withRequirement(commandSender -> commandSender instanceof Player)
                .executesPlayer((player, objects) -> {
                    StaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
                    staffPlayer.toggleStaffMode();
                })
                .register();
    }
}
