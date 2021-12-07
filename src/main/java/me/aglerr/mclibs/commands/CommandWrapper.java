package me.aglerr.mclibs.commands;

import org.bukkit.command.CommandSender;

public class CommandWrapper {

    private final CommandSender sender;
    private final String[] args;

    public CommandWrapper(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

}
