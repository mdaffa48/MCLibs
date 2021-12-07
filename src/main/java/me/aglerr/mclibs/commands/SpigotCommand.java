package me.aglerr.mclibs.commands;

import me.aglerr.mclibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SpigotCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final JavaPlugin plugin;
    private final @NotNull String COMMAND_NAME;
    private final @NotNull List<String> COMMAND_ALIASES;
    private final @Nullable String COMMAND_PERMISSION;
    private final Consumer<CommandSender> onNoArgs;
    private final Consumer<CommandSender> onNoPermission;
    private final Consumer<CommandSender> onNoSubcommand;

    public SpigotCommand(JavaPlugin plugin, @NotNull String name, @NotNull List<String> aliases,
                         @Nullable String COMMAND_PERMISSION, Consumer<CommandSender> onNoArgs,
                         Consumer<CommandSender> onNoPermission, Consumer<CommandSender> onNoSubcommand,
                         SubCommand... subCommands) {
        this.plugin = plugin;
        this.COMMAND_NAME = name;
        this.COMMAND_ALIASES = aliases;
        this.COMMAND_PERMISSION = COMMAND_PERMISSION;
        this.onNoArgs = onNoArgs;
        this.onNoPermission = onNoPermission;
        this.onNoSubcommand = onNoSubcommand;

        for (SubCommand subCommand : subCommands) {
            this.subCommandMap.put(subCommand.getName(), subCommand);
        }
    }

    public void register() {
        plugin.getCommand(COMMAND_NAME).setExecutor(this);
        plugin.getCommand(COMMAND_NAME).setTabCompleter(this);
        plugin.getCommand(COMMAND_NAME).getAliases().addAll(COMMAND_ALIASES);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            ((CommandMap) bukkitCommandMap.get(Bukkit.getServer())).register(COMMAND_NAME, plugin.getCommand(COMMAND_NAME));
            bukkitCommandMap.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Common.log("&cFailed to register command '" + COMMAND_NAME + "'");
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(COMMAND_PERMISSION != null && !sender.hasPermission(COMMAND_PERMISSION)){
            onNoPermission.accept(sender);
            return true;
        }
        if (args.length == 0) {
            onNoArgs.accept(sender);
            return true;
        }
        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
        if (subCommand == null) {
            onNoSubcommand.accept(sender);
            return true;
        }
        if (subCommand.getPermission() != null &&
                !sender.hasPermission(subCommand.getPermission())) {
            onNoPermission.accept(sender);
            return true;
        }
        subCommand.execute(plugin, sender, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (SubCommand subCommand : subCommandMap.values()) {
                if (subCommand.getPermission() == null) {
                    suggestions.add(subCommand.getName());
                    continue;
                }
                if (sender.hasPermission(subCommand.getPermission())) {
                    suggestions.add(subCommand.getName());
                }
            }
            return suggestions;
        }
        if (args.length >= 2) {
            SubCommand subCommand = subCommandMap.get(args[0].toLowerCase());
            if (subCommand == null) {
                return new ArrayList<>();
            }
            if (subCommand.getPermission() == null ||
                    sender.hasPermission(subCommand.getPermission())) {
                return subCommand.parseTabCompletions(plugin, sender, args);
            }
        }
        return new ArrayList<>();
    }
}
