package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.PlayerStats;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class StatsListener implements Listener {


    private final SurvivalEngine plugin;

    public StatsListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerStats stats = getStats(player.getUniqueId());

        stats.addBocksDestroyed(1);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        PlayerStats stats = getStats(player.getUniqueId());

        stats.addBocksPlaced(1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        PlayerStats stats = getStats(e.getEntity().getUniqueId());
        Player killer = e.getEntity().getKiller();

        if(killer != null) {
            PlayerStats killerStats = getStats(killer.getUniqueId());
            killerStats.setPlayerKills(killerStats.getPlayerKills() + 1);
        }
        stats.addDeaths(1);
        plugin.getTabListManager().update();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player)) {
            Player killer = e.getEntity().getKiller();
            if (killer == null) { return; }
            PlayerStats killerStats = getStats(killer.getUniqueId());
            killerStats.setMobKills(killerStats.getMobKills() + 1);
        };

    }



    private PlayerStats getStats(UUID uuid) {
        SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(uuid);
        return survivalPlayer.getStatistics();
    }

}
