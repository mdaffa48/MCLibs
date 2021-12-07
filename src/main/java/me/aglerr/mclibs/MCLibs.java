package me.aglerr.mclibs;

import me.aglerr.mclibs.commands.SpigotCommand;
import me.aglerr.mclibs.inventory.SimpleInventoryManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class MCLibs {

    public static Plugin INSTANCE;

    public static void init(Plugin plugin){
        INSTANCE = plugin;
        SimpleInventoryManager.register(plugin);
    }

    public void registerCommand(){
        new SpigotCommand(INSTANCE, "cosmetics", Arrays.asList("a"), "permission",
                wrapper -> {
                    if(wrapper.getSender() instanceof Player){
                        Player sender = (Player) wrapper.getSender();
                    }
                }
        ).register();
    }

}
