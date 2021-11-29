package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.PlayerChatInput;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TeleporterPlaceListener implements Listener {


    private final SurvivalEngine plugin;

    public TeleporterPlaceListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }



    @EventHandler
    public void onTeleporterPlace (TeleporterPlaceEvent e) {
        Location teleporterLocation = e.getBlock().getLocation().add(0,2,0);
        new PlayerChatInput(plugin, e.getPlayer(), "Benenne den Teleporter!", (input) -> {
            if(input == null) {
                e.setCancelled(true);
                return;
            }
            Entity teleporter = teleporterLocation.getWorld().spawnEntity(teleporterLocation, EntityType.ENDER_CRYSTAL);
            teleporter.setCustomName("§c" + input);
            teleporter.setCustomNameVisible(true);

        });
    }



}
