package me.aglerr.mclibs.libs;

import org.bukkit.Bukkit;

public class Debug {

    private static boolean ENABLED = false;

    public static void enable(){
        ENABLED = true;
    }

    public static void disable(){
        ENABLED = false;
    }

    public static void send(String... messages) {
        if (!ENABLED) {
            return;
        }
        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    Common.PREFIX + " " + Common.color(message)
            );
        }
    }

}
