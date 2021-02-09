package de.hglabor.utils.noriskutils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {
    private final String name;
    private String permission;

    public AbstractCommand(String name) {
        this.name = name;
    }

    public AbstractCommand(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            return true;
        }
        if (commandSender instanceof Player && command.getName().equalsIgnoreCase(name)) {
            if (permission != null) {
                return commandSender.hasPermission(permission);
            }
            return true;
        }
        return false;
    }
}
