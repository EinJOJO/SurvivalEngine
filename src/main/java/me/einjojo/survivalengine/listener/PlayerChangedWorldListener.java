package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.TransporterManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {



    private final TransporterManager transporterManager;

    public PlayerChangedWorldListener(SurvivalEngine plugin) {
        transporterManager = plugin.getTransportManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        TransportChicken chicken = transporterManager.getTransportChicken(e.getPlayer().getUniqueId());

        if(chicken != null) {
            if(chicken.isSpawned()) {
                chicken.remove();
                chicken.spawn(e.getPlayer());
            }
        }
    }

}
