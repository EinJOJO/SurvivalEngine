package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

public class AnvilListener implements Listener {

    public AnvilListener (SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void removeLevelCap(PrepareAnvilEvent e) {
        AnvilInventory inventory = e.getInventory();
        if(inventory.getRepairCost() > inventory.getMaximumRepairCost()) {
            inventory.setRepairCost(inventory.getMaximumRepairCost() - 1);
        }
    }
}
