package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SurvivalEngine plugin;

    public PlayerJoinListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getPlayerManager().createPlayer(player);
        plugin.getRecipeManager().loadRecipes(player);
        plugin.getTabListManager().setPlayerList(player);
        plugin.getTabListManager().registerTeam(player);
        TransportChicken chicken = plugin.getTransportManager().getTransportChicken(player.getUniqueId());
        if(chicken != null) {
            if(chicken.isSpawned()) {
                chicken.spawn(player);
            }
        }

        e.setJoinMessage(plugin.getPREFIX() + "§e" + player.getName() + "§7 hat den Server §abetreten");
        player.resetTitle();
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendTitle("§b§lWillkommen!", "", 15, 30, 15);
    }


    @EventHandler
    public void overrideSpawn(PlayerJoinEvent e) {
        SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());

        if(survivalPlayer.isResetSpawn()) {
            e.getPlayer().teleport(new Location(Bukkit.getWorld("world"),-2, 68, 10));
            survivalPlayer.setResetSpawn(false);
        }

    }


}
