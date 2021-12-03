package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityDismountListener implements Listener {


    private final TeleportManager teleportManager;
    private final SurvivalEngine plugin;

    public EntityDismountListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.teleportManager = plugin.getTeleportManager();
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleportWhileInVehicle(EntityDismountEvent event) {
        if(event.isCancelled()) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Entity vehicle = event.getDismounted();
        if(teleportManager.getTELEPORTING_PLAYERS().contains(player)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->{
                List<Entity> teleported = new ArrayList<>();
                teleported.add(player);
                vehicle.getPassengers().forEach((entity -> {
                    entity.teleport(player.getLocation());
                    teleported.add(entity);
                }));
                teleported.add(player);
                vehicle.teleport(player.getLocation());

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,()->{
                    teleported.forEach(vehicle::addPassenger);
                }, 6L);
            },2L);
        }
    }

}
