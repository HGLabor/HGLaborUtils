package de.hglabor.utils.noriskutils.staffmode;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.entity.Player;

public class StaffModeCommand {

    public StaffModeCommand(StaffPlayerSupplier staffPlayerSupplier) {
        new CommandAPICommand("staffmode")
                .withPermission("hglabor.staffmode")
                .withArguments(new GreedyStringArgument("confirm"))
                .withRequirement(commandSender -> commandSender instanceof Player)
                .executesPlayer((player, objects) -> {
                    StaffPlayer staffPlayer = staffPlayerSupplier.getStaffPlayer(player);
                    staffPlayer.toggleStaffMode();
                })
                .register();
    }
}
