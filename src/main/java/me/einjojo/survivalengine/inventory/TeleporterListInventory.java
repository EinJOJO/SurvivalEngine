package me.einjojo.survivalengine.inventory;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.InventoryManager;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TeleporterListInventory {

    private final static String name = "§eZu Teleporter Teleportieren";
    private Inventory inventory = Bukkit.createInventory(null, 54, name);
    private final InventoryManager inventoryManager;

    public TeleporterListInventory() {
        this.inventoryManager = SurvivalEngine.getInstance().getInventoryManager();
    }


    public static String getName() {
        return name;
    }

    public void openInventory(Player player, Teleporter teleporter) {
        player.openInventory(getInventory(player, teleporter));
    }


    public Inventory getInventory(Player player, Teleporter teleporter) {
        inventoryManager.fillGuiWithGlass(inventory);
        int i = 9;
        inventoryManager.addItemStack(inventory, new ItemStack(Material.ARROW), 49, "§cZurück");
        for (String name : teleporter.getLINKED_TELEPORTER()) {
            if(i > 44) { return inventory; }
            addTeleporter("§c"+name, i);
            i++;
        }

        return inventory;
    }


    private void addTeleporter(String name, int pos) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7[§cLinksklick§7] Teleportieren.");
        lore.add("");
        inventoryManager.addItemStack(inventory, new ItemStack(Material.PAPER), pos, name, lore);
    }



}
