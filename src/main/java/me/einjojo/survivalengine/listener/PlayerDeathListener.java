package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;
import java.util.logging.Level;

public class PlayerDeathListener implements Listener {

    private final SurvivalEngine plugin;

    public PlayerDeathListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void sendDeathMessage(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.setDeathMessage(plugin.getPREFIX() + Objects.requireNonNull(e.getDeathMessage()).replace(player.getName(), "§e" + player.getName() + "§7"));

        player.sendMessage("\n" + plugin.getPREFIX() + "§cDu bist gestorben.");
        Location location = player.getLocation();
        String locationString = String.format("%d %d %d",(int) location.getX(), (int) location.getY(), (int) location.getZ());
        player.sendMessage(String.format("%sPosition: §c%s", plugin.getPREFIX(), locationString));
        player.sendMessage("");
        plugin.getLogger().log(Level.INFO, String.format("Player Death: %s died at %s", player.getName(), locationString));
    }



}
