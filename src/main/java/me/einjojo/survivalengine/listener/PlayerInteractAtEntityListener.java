package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

public class PlayerInteractAtEntityListener implements Listener {

    private final SurvivalEngine plugin;
    public PlayerInteractAtEntityListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void openTeleporterGUI(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        if(entity.getType() != EntityType.ENDER_CRYSTAL) return;
        String teleporterName = entity.getCustomName();
        if(teleporterName.equals("")) return;
        if(!entity.isCustomNameVisible()) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItem(e.getHand()).isSimilar(TeleportCrystalRecipe.getItemStack())) return;
        Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterName);

        if(teleporter == null) {
            player.sendMessage(plugin.getPREFIX() + "Der Teleporter war nicht gültig und wurde entfernt");
            entity.remove();
            return;
        }

        if(teleporter.isActivated()) {
            entity.setInvulnerable(false);
        } else {
            entity.setInvulnerable(true);
        }

        Inventory inventory = player.getInventory();
        player.openInventory(inventory);
    }


}
