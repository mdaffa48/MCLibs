package me.aglerr.mclibs;

import me.aglerr.mclibs.inventory.SimpleInventoryManager;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MCLibs {

    public static JavaPlugin INSTANCE;
    public static boolean PLACEHOLDER_API;

    public static void init(JavaPlugin plugin){
        INSTANCE = plugin;
        SimpleInventoryManager.register(plugin);
        PLACEHOLDER_API = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
