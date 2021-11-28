package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;

import java.util.UUID;

public class BlockPlaceListener implements Listener {

    private final SurvivalEngine plugin;
    public BlockPlaceListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void TeleportPlace2 (EntityPlaceEvent e) {
       if (!(e.getEntity() instanceof CraftEnderCrystal)) {
           return;
       }

       if(!(e.getBlock().getType().equals(Material.OBSIDIAN))) {
           return;
       }
       e.getEntity().setInvulnerable(true);
        e.getBlock().getWorld().strikeLightning(e.getBlock().getLocation());
        final Player player = e.getPlayer();
        player.sendMessage(plugin.getPREFIX() + "Der Teleporter wird eingerichtet...");
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,()->{
            e.getEntity().setInvulnerable(false);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 85, 48);
            player.sendMessage(plugin.getPREFIX() + "Der Teleporter wurde eingerichtet.");
        }, 60);
    }
}
