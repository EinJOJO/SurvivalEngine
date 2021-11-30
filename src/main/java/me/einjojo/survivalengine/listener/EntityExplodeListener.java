package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    private final SurvivalEngine plugin;

    public EntityExplodeListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterDeath(EntityExplodeEvent e) {
        Entity teleportCrystal = e.getEntity();
        if(e.getEntity().getType() != EntityType.ENDER_CRYSTAL) return;
        if(!teleportCrystal.isCustomNameVisible()) return;
        if(teleportCrystal.getCustomName() == null || teleportCrystal.getCustomName().equals("")) return;
        Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleportCrystal.getCustomName());
        if(teleporter == null) return;
        Player owner = Bukkit.getPlayer(teleporter.getOwner());
        if(owner != null) owner.sendMessage(plugin.getPREFIX() + String.format("Dein Teleporter §c%s §7wurde zerstört!", teleporter.getName()));
        plugin.getTeleportManager().deleteTeleporter(teleporter);
        e.setCancelled(true);
        e.getLocation().getWorld().createExplosion(e.getLocation(), 2, true, true);
    }

}
