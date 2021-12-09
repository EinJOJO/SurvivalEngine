package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.inventory.TeleporterListInventory;
import me.einjojo.survivalengine.inventory.TeleporterMainInventory;
import me.einjojo.survivalengine.manager.InventoryManager;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.TeleportCrystalUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {


    private final SurvivalEngine plugin;
    private final InventoryManager inventoryManager;
    private final TeleportManager teleportManager;

    public InventoryClickListener(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.inventoryManager = plugin.getInventoryManager();
        this.teleportManager = plugin.getTeleportManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleporterInventoryClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(TeleporterMainInventory.getName())) return;
        ItemStack itemStack = e.getCurrentItem();
        if ((itemStack == null) || (!itemStack.hasItemMeta()) || (itemStack.getItemMeta().getDisplayName().equals(TeleportCrystalRecipe.getItemStack().getItemMeta().getDisplayName())))
            return;

        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        String teleporterName = e.getInventory().getItem(13).getItemMeta().getDisplayName();
        Teleporter teleporter = plugin.getTeleportManager().getTeleporter(teleporterName);

        if(teleporter == null) {
            player.closeInventory();
            player.sendMessage("§cKaputt.");
            return;
        }

        inventoryManager.setTeleporterInventory(player, teleporter);

        switch (itemStack.getItemMeta().getDisplayName()) {
            case "§4Teleporter abbauen":
                plugin.getTeleportManager().deleteTeleporterAndEntity(teleporter, player);
                player.getInventory().addItem(TeleporterRecipe.getItemStack());
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 2);
                player.closeInventory();
                player.sendMessage(plugin.getPREFIX() + "Du hast den Teleporter abgebaut");
                break;
            case "§6Verlinkte Teleporter":
                if(e.getClick().equals(ClickType.RIGHT)) {
                    if(!teleportManager.hasShardCoolDown(teleporter)) {
                        ItemStack shard = new TeleportCrystalUtil().getTeleporterShard(teleporter);
                        player.getInventory().addItem(shard);
                        player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 0.5f, 1);
                        teleportManager.setShardCoolDown(teleporter);
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
                        player.sendMessage(plugin.getPREFIX() + "§cEin Splitter wurde erst kürzlich genommen.");
                    }
                } else if (e.getClick().equals(ClickType.LEFT)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    new TeleporterListInventory().openInventory(player, teleporter);
                } else {
                    return;
                }
                break;
        }
    }

    @EventHandler
    public void onTeleporterListInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(!e.getView().getTitle().equals(TeleporterListInventory.getName())) {
            return;
        }
        ItemStack itemStack = e.getCurrentItem();
        if(itemStack == null) {
            return;
        }
        if ((!itemStack.hasItemMeta())) {
            return;
        }
        e.setCancelled(true);

        switch (itemStack.getItemMeta().getDisplayName()) {
            case "":
                return;
            case "§cZurück":
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                new TeleporterMainInventory().openInventory(player, inventoryManager.getTeleporter(player));
                break;
            default:
                if((itemStack.getItemMeta() == null) || (itemStack.getItemMeta().getLore() == null)) {
                    return;
                }
                if(!itemStack.getItemMeta().getLore().get(1).contains("§7[§cLinksklick§7]")) {
                    return;
                }

                String targetTeleporterName = itemStack.getItemMeta().getDisplayName();
                Teleporter currentTeleporter = inventoryManager.getTeleporter(player);
                Teleporter targetTeleporter = teleportManager.getTeleporter(targetTeleporterName);

                if(targetTeleporter == null) {
                    currentTeleporter.unLink(targetTeleporterName);
                    player.sendMessage("§cDer Teleporter existiert nicht mehr");
                    player.closeInventory();
                    return;
                }

                if(e.getClick().equals(ClickType.LEFT)) {
                    player.closeInventory();
                    teleportManager.teleport(player, targetTeleporter);
                }
        }
    }
}
