package me.einjojo.survivalengine.util;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerChatInput implements Listener {

    private final SurvivalEngine plugin;
    private final BukkitTask taskID;
    private final Runnable callback;
    private final UUID playerUUID;

    private static final Map<UUID, PlayerChatInput> inputs = new HashMap<>();

    public PlayerChatInput(SurvivalEngine plugin, Player player, String title, Runnable callback) {
        this.plugin = plugin;

        this.taskID = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(title, "§7Gib §ccancel §7ein, um abzubrechen", 0, 25 ,0);
            }
        }.runTaskTimer(plugin, 0, 20);

        this.playerUUID = player.getUniqueId();
        this.callback = callback;

        register();
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e) {
        final String input = e.getMessage();
        final Player player = e.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        if(!inputs.containsKey(playerUUID)) {
            return;
        }
        PlayerChatInput current = inputs.get(playerUUID);

        e.setCancelled(true);


        current.taskID.cancel();
        current.unregister();
        player.resetTitle();
        if(input.equalsIgnoreCase("cancel")) {
            player.sendMessage(plugin.getPREFIX() + "§cabgebrochen.");
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> current.callback.run(input), 3);

    }

    private void register() {
        inputs.put(this.playerUUID, this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void unregister() {
        inputs.remove(this.playerUUID);
        HandlerList.unregisterAll(this);
    }

    @FunctionalInterface
    public interface Runnable {
        void run(String input);
    }



}
