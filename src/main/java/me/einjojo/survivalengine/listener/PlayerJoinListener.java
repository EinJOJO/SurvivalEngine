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
        plugin.getPlayerManager().createPlayer(player);
        plugin.getRecipeManager().loadRecipes(player);
        plugin.getTabListManager().setPlayerList(player);
        plugin.getTabListManager().registerTeam(player);

        e.setJoinMessage(plugin.getPREFIX() + "§e" + player.getName() + "§7 hat den Server §abetreten");
        player.resetTitle();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendTitle("§b§lWillkommen!", "", 15, 20, 15);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            player.sendTitle("§c§lChunks werden geladen!", "§7Während des Prozesses bist du im Void, also keine Sorge.", 20, 30, 20);
        }, 50);
    }
}
