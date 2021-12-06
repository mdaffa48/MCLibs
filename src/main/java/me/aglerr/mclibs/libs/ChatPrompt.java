package me.aglerr.mclibs.libs;

import me.aglerr.mclibs.MCLibs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ChatPrompt implements Listener {

    private static final Map<Player, Prompt> prompts = new HashMap<>();

    static {
        new ChatPrompt();
    }

    private ChatPrompt() {
        Bukkit.getPluginManager().registerEvents(this, MCLibs.INSTANCE);
    }

    public static void prompt(Player player, String message, Consumer<String> onResponse, Consumer<CancelReason> onCancel){
        Prompt removed = prompts.remove(player);
        if(removed != null){
            removed.cancel(CancelReason.PROMPT_OVERRIDEN);
        }
        prompts.put(player, new Prompt(onResponse, onCancel));
        Optional.of(Common.color(message)).ifPresent(player::sendMessage);
    }

    public static void prompt(Player player, String message, Consumer<String> onResponse){
        prompt(player, message, onResponse, c -> {});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Prompt prompt = prompts.remove(event.getPlayer());
        if(prompt == null){
            return;
        }
        event.setCancelled(true);
        if(event.getMessage().equalsIgnoreCase("cancel")){
            prompt.cancel(CancelReason.PLAYER_CANCELLED);
            return;
        }
        prompt.respond(event.getMessage());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Prompt prompt = prompts.remove(event.getPlayer());
        if(prompt != null){
            prompt.cancel(CancelReason.PLAYER_LEFT);
        }
    }

    private static class Prompt {

        private final Consumer<String> onResponse;
        private final Consumer<CancelReason> onCancel;

        public Prompt(Consumer<String> onResponse, Consumer<CancelReason> onCancel) {
            this.onResponse = onResponse;
            this.onCancel = onCancel;
        }

        public void respond(String message) {
            onResponse.accept(message);
        }

        public void cancel(CancelReason cancelReason) {
            onCancel.accept(cancelReason);
        }

    }

    public enum CancelReason {
        PROMPT_OVERRIDEN,
        PLAYER_CANCELLED,
        PLAYER_LEFT
    }

}
