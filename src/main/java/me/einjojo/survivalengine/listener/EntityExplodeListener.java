package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Random;

public class EntityExplodeListener implements Listener {

    private final SurvivalEngine plugin;

    public EntityExplodeListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterDeath(EntityExplodeEvent e) {
        Entity teleportCrystal = e.getEntity();
        if (e.getEntity().getType() != EntityType.ENDER_CRYSTAL) return;
        if (!teleportCrystal.isCustomNameVisible()) return;
        if (teleportCrystal.getCustomName() == null || teleportCrystal.getCustomName().equals("")) return;
        die(teleportCrystal);
        e.setCancelled(true);

    }

    @EventHandler
    public void onTeleporterDamage(EntityDamageEvent e) {
        Entity teleportCrystal = e.getEntity();
        if (e.getEntity().getType() != EntityType.ENDER_CRYSTAL) return;
        if (!teleportCrystal.isCustomNameVisible()) return;
        if (teleportCrystal.getCustomName() == null || teleportCrystal.getCustomName().equals("")) return;
        e.setCancelled(true);
        die(teleportCrystal);
    }

    private void die(Entity teleportCrystal) {
        Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleportCrystal.getCustomName());
        if (teleporter == null) return;
        Player owner = Bukkit.getPlayer(teleporter.getOwner());
        if (owner != null)
            owner.sendMessage(plugin.getPREFIX() + String.format("Dein Teleporter §c%s §7wurde zerstört!", teleporter.getName()));
        plugin.getTeleportManager().deleteTeleporter(teleporter);
        teleportCrystal.getLocation().getWorld().createExplosion(teleportCrystal.getLocation(), 3, true, true);
        Random r = new Random();
        if (r.nextInt(10) == 4) {
            teleportCrystal.getLocation().getWorld().dropItem(teleportCrystal.getLocation(), TeleporterRecipe.getItemStack());
        }
        ;
        teleportCrystal.remove();
    }

}
