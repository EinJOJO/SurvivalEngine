package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class BedListener implements Listener {

    private final SurvivalEngine plugin;

    public BedListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }


    public void onBedEnter(PlayerBedEnterEvent e) {

    }

    public void onBedLeave(PlayerBedLeaveEvent e) {

    }
}
