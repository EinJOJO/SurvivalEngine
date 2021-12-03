package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    public PlayerChatListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            e.setCancelled(true);
            Bukkit.broadcastMessage(String.format("§7[§e%s§7] §7%s §8» §7%s", e.getPlayer().getName(), e.getMessage()));
        }
    }

}
