package de.hglabor.utils.noriskutils.command;

import de.hglabor.utils.noriskutils.staffmode.PlayerHider;
import de.hglabor.utils.noriskutils.staffmode.StaffModeManager;
import de.hglabor.utils.noriskutils.staffmode.StaffPlayer;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;

public class HidePlayersCommand {

    public HidePlayersCommand() {
        new CommandAPICommand("showspecs")
                .withPermission("hglabor.staffmode")
                .withArguments(new BooleanArgument("show"))
                .executesPlayer((player, objects) -> {
                    boolean showPlayers = (boolean) objects[0];
                    PlayerHider playerHider = StaffModeManager.INSTANCE.getPlayerHider();
                    playerHider.getSupplier().getStaffPlayer(player).setCanSeeStaffModePlayers(showPlayers);
                    if (showPlayers) {
                        playerHider.showEveryoneInStaffMode(player);
                    } else {
                        playerHider.hideEveryoneInStaffMode(player);
                    }
                })
                .register();
    }
}
