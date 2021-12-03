package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.PlayerChatInput;
import org.bukkit.Bukkit;
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

            if(input.length() > 24) {
                e.getPlayer().sendMessage(plugin.getPREFIX() + "§cDer Name darf nicht länger als 24 Zeichen sein.");
                e.setCancelled(true);
                return;
            }

            Teleporter teleporter = new Teleporter(input, teleporterLocation, e.getPlayer().getUniqueId());
            if(plugin.getTeleportManager().createTeleporter(teleporter)) {
                Entity teleporterEntity = teleporterLocation.getWorld().spawnEntity(teleporterLocation, EntityType.ENDER_CRYSTAL);
                teleporterEntity.setCustomName("§c" + input);
                teleporterEntity.setCustomNameVisible(true);
                e.getPlayer().sendMessage(plugin.getPREFIX() + "§aDer Teleporter wurde platziert");
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage(plugin.getPREFIX() + "§cDieser Name wird bereits verwendet.");

            }
        });
    }
}
