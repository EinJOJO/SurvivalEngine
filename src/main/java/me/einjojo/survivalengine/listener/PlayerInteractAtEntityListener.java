package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.util.TeleportCrystalUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityListener implements Listener {

    private final SurvivalEngine plugin;
    public PlayerInteractAtEntityListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void openTeleporterGUI(PlayerInteractAtEntityEvent e) {
        if(e.getHand().equals(EquipmentSlot.HAND)) {
            if(isInteractingWithTeleporter(e)) {
                Entity entity = e.getRightClicked();
                Player player = e.getPlayer();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                String teleporterName = entity.getCustomName();
                Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterName);
                if(itemStack.isSimilar(TeleportCrystalRecipe.getItemStack())) return;
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
    }

    @EventHandler
    public void bindCrystalToTeleporter(PlayerInteractAtEntityEvent e)  {
        if(e.getHand().equals(EquipmentSlot.HAND)) {
            if(isInteractingWithTeleporter(e)) {
                Player player = e.getPlayer();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Entity teleporterEntity = e.getRightClicked();
                if(!itemStack.isSimilar(TeleportCrystalRecipe.getItemStack())) return;

                Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterEntity.getName());
                TeleportCrystalUtil crystalUtil = new TeleportCrystalUtil();
                ItemStack boundCrystal = crystalUtil.bind(itemStack, teleporter);

                player.sendMessage(plugin.getPREFIX() + "Der Kristall wurde an §c" + teleporter.getName() + " §7gebunden.");
            }
        }
    }


    private boolean isInteractingWithTeleporter(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        if(entity.getType() != EntityType.ENDER_CRYSTAL) return false;
        if(entity.getCustomName() == null) return false;
        if(entity.getCustomName().equals("")) return false;
        return entity.isCustomNameVisible();
    }


}
