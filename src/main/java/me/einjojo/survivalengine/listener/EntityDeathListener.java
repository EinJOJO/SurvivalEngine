package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.manager.TransporterManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ConcurrentModificationException;

public class EntityDeathListener implements Listener {


    private final TransporterManager mobManager;

    public EntityDeathListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.mobManager = plugin.getTransportManager();
    }


    @EventHandler
    public void onChickenDeath (EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if(entity.getType() != EntityType.CHICKEN) {
            return;
        }
        TransportChicken transportChicken = mobManager.getTransportChicken(entity);
        if(transportChicken == null) {
            return;
        }
        try {
            transportChicken.getInventory().getViewers().forEach(HumanEntity::closeInventory);
        } catch (ConcurrentModificationException ex) { /* ignore */}
        transportChicken.getInventory().forEach((itemStack -> {
            if(itemStack != null) {
                entity.getWorld().dropItem(entity.getLocation(), itemStack);
            }
        }));
        Player chickenOwner = Bukkit.getPlayer(transportChicken.getOwner());
        mobManager.removeChicken(transportChicken.getOwner());
        if(chickenOwner != null) {
            chickenOwner.sendMessage("§cDein Transporter ist verstorben...");
        }

    }

}
