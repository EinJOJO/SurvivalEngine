package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SurvivalEngine plugin;

    public PlayerJoinListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(plugin.getPREFIX() + "§e" + player.getName() + "§7 hat den Server §abetreten");

        plugin.recipeManager.loadRecipes(player);
        player.resetTitle();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendTitle("§b§lWillkommen!", "", 25, 50, 25);
        plugin.tabListManager.setPlayerList(player);
        plugin.tabListManager.setPlayerTeams(player);
    }
}
