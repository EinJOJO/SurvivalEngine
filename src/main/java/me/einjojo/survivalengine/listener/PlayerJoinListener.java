package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private SurvivalEngine plugin;

    public PlayerJoinListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(plugin.getPREFIX() + "§e" + player.getName() + "§7 hat den Server §abetreten");

        player.resetTitle();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendTitle("§b§lWillkommen!", "", 25, 50, 25);

    }
}
