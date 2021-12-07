package me.aglerr.mclibs;

import me.aglerr.mclibs.inventory.SimpleInventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCLibs {

    public static JavaPlugin INSTANCE;

    public static void init(JavaPlugin plugin){
        INSTANCE = plugin;
        SimpleInventoryManager.register(plugin);
    }

}
