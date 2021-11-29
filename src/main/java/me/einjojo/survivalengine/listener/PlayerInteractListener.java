package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.events.TeleporterPlaceEvent;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final SurvivalEngine plugin;

    public PlayerInteractListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void placeTeleporter(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(e.getItem() == null) return;
        if(!e.getItem().isSimilar(TeleporterRecipe.getItemStack())) return;
        Block block = e.getClickedBlock();
        if(block == null) return;
        e.setCancelled(true); // Don't Place it

        if(e.getBlockFace().equals(BlockFace.UP)) {
            Player player = e.getPlayer();
            e.getItem().setAmount(e.getItem().getAmount() - 1);
            TeleporterPlaceEvent teleporterPlaceEvent = new TeleporterPlaceEvent(player, block);
            Bukkit.getPluginManager().callEvent(teleporterPlaceEvent);

            if(teleporterPlaceEvent.isCancelled()) {
                player.getInventory().addItem(TeleporterRecipe.getItemStack());
            }

        };
    }
}
