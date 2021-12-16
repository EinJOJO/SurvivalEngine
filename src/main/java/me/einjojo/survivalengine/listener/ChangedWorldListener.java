package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.TransporterManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
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
                }, 80L);
            }
        }
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent e) {
        if(!e.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }
        Player player = (Player) e.getEntity();

    }


    @EventHandler
    public void preventChickenFromEntering(EntityPortalEnterEvent e) {
        if(!e.getEntity().getType().equals(EntityType.CHICKEN)) {
            return;
        }
        Entity entity = e.getEntity();
        entity.remove();
    }
}
