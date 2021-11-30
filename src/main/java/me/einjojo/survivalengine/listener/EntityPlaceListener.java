package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;

import java.util.UUID;

public class EntityPlaceListener implements Listener {

    private final SurvivalEngine plugin;
    public EntityPlaceListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterPlace(EntityPlaceEvent e) {
       if (!(e.getEntity() instanceof CraftEnderCrystal)) {
           return;
       }
       if(!(e.getBlock().getType().equals(Material.OBSIDIAN))) {
           return;
       }
       e.getEntity().setInvulnerable(true);
        e.getBlock().getWorld().strikeLightningEffect(e.getBlock().getLocation());
        final Player player = e.getPlayer();
        player.sendMessage(plugin.getPREFIX() + "Der Teleporter wird eingerichtet...");
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,()->{
            e.getEntity().setInvulnerable(false);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 85, 48);
            player.sendMessage(plugin.getPREFIX() + "Der Teleporter wurde eingerichtet.");
        }, 60);
    }
}
