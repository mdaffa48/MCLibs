package me.aglerr.mclibs;

import me.aglerr.mclibs.inventory.SimpleInventoryManager;
import org.bukkit.plugin.Plugin;

public class MCLibs {

    public static Plugin INSTANCE;

    public static void init(Plugin plugin){
        INSTANCE = plugin;
        SimpleInventoryManager.register(plugin);
    }

}
