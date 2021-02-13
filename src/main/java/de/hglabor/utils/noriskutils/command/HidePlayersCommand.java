package de.hglabor.utils.noriskutils.command;

import de.hglabor.utils.noriskutils.HideUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;

public class HidePlayersCommand {

    public HidePlayersCommand() {
        new CommandAPICommand("showspecs")
                .withPermission("hglabor.staffmode")
                .withArguments(new BooleanArgument("show"))
                .executesPlayer((player, objects) -> {
                    boolean showPlayers = (boolean) objects[0];
                    if (showPlayers) {
                        //TODO alle specs shown
                        HideUtils.INSTANCE.remove(player);
                    } else {
                        //TODO alle specs hiden
                        HideUtils.INSTANCE.put(player);
                    }
                })
                .register();
    }
}
