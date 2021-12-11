package me.aglerr.mclibs.libs;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

public class Common {

    public static String PREFIX = "[MCLibs]";

    public static void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    public static void sendMessage(CommandSender sender, List<String> messages) {
        messages.forEach(message -> sendMessage(sender, message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static List<String> color(List<String> messages) {
        return messages.stream().map(Common::color).collect(Collectors.toList());
    }

    public static String color(String message) {
        return BaseComponent.toLegacyText(MineDown.parse(message));
    }

    public static void log(String... messages) {
        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(
                    Common.PREFIX + " " + color(message)
            );
        }
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean hasCustomModelData() {
        return Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") ||
                Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17") ||
                Bukkit.getVersion().contains("1.18");
    }

    public static boolean supportHexColor() {
        return Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17") ||
                Bukkit.getVersion().contains("1.18");
    }

    public static boolean hasOffhand() {
        return !Bukkit.getVersion().contains("1.7") &&
                !Bukkit.getVersion().contains("1.8");
    }

    public static String digits(double d){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);
        return numberFormat.format(d);
    }

}
