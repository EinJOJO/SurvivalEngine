package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeleporterPlaceListener implements Listener {

    public TeleporterPlaceListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterPlace (TeleporterPlaceEvent e) {
        Bukkit.broadcastMessage("TeleporterPlaced " + e.getPlayer().getName());
    }

}
