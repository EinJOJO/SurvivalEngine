package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.BedManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BedListener implements Listener {

    private final BedManager bedManager;

    public BedListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.bedManager = plugin.getBedManager();
    }


    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if(e.isCancelled()) return;
        bedManager.addPlayer(e.getPlayer());

    }
    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent e) {
        if(e.isCancelled()) return;
        bedManager.removePlayer(e.getPlayer());
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        bedManager.removePlayer(e.getPlayer());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        bedManager.removePlayer(e.getPlayer());
    }
}
