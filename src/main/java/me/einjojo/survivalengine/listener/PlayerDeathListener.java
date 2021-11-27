package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final SurvivalEngine plugin;

    public PlayerDeathListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void sendDeathMessage(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.setDeathMessage(plugin.getPREFIX() + e.getDeathMessage());
        player.sendMessage();
    }

}
