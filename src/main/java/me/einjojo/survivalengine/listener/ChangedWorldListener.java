package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.TransporterManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangedWorldListener implements Listener {



    private final TransporterManager transporterManager;
    private final SurvivalEngine plugin;

    public ChangedWorldListener(SurvivalEngine plugin) {
        transporterManager = plugin.getTransportManager();
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent e) {
        TransportChicken chicken = transporterManager.getTransportChicken(e.getPlayer().getUniqueId());

        if(chicken != null) {
            if(chicken.isSpawned()) {
                chicken.remove();
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    transporterManager.changedWorld(e.getPlayer());
                }, 30L);
            }
        }
    }

    @EventHandler
    public void onPortalExit(EntityPortalExitEvent e) {
        if(e.getEntity().getType().equals(EntityType.CHICKEN)) {
            if(e.getEntity().getCustomName().contains("Transporter")){
                e.getEntity().remove();
            }
        }
    }

}
