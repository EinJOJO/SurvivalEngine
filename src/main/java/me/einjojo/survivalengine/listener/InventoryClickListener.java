package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.inventory.TeleporterInventory;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {


    private final SurvivalEngine plugin;

    public InventoryClickListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(TeleporterInventory.getName())) return;
        ItemStack itemStack = e.getCurrentItem();
        if ((itemStack == null) || (!itemStack.hasItemMeta()) || (itemStack.getItemMeta().getDisplayName().equals(TeleportCrystalRecipe.getItemStack().getItemMeta().getDisplayName())))
            return;

        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        switch (itemStack.getItemMeta().getDisplayName()) {
            case "§4Teleporter abbauen":
                String teleporterName = e.getInventory().getItem(13).getItemMeta().getDisplayName();
                Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterName);
                if (teleporter != null) {
                    plugin.getTeleportManager().deleteTeleporter(teleporter, player);
                    player.getInventory().addItem(TeleporterRecipe.getItemStack());
                    player.closeInventory();
                    player.sendMessage(plugin.getPREFIX() + "Du hast den Teleporter abgebaut");
                }
                break;
        }

    }

}
