package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    SurvivalEngine plugin;

    public PlayerChatListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            e.setCancelled(true);
            SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
            if(survivalPlayer.getTeam() == null) {
                Bukkit.broadcastMessage(String.format("§7%s §8» §7%s", e.getPlayer().getName(), e.getMessage()));
            } else {
                Bukkit.broadcastMessage(String.format("§7[§e%s§7] §7%s §8» §7%s", survivalPlayer.getTeam().getName(), e.getPlayer().getName(), e.getMessage()));
            }
        }
    }

}
