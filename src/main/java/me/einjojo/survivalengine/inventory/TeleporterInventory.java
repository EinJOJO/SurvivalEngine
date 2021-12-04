package me.einjojo.survivalengine.inventory;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.InventoryManager;
import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleporterInventory {

    private final SurvivalEngine plugin;
    private final InventoryManager inventoryManager;
    private final static String name = "§cTeleporter verwalten";

    public TeleporterInventory(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.inventoryManager = plugin.getInventoryManager();
    }

    public static String getName() {
        return name;
    }


    public Inventory getInventory(Player player, Teleporter teleporter) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, name);
        inventoryManager.fillGuiWithGlass(inventory);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Platziere deinen §5Teleport");
        lore.add("§5Kristall §7hier rein, um");
        lore.add("§7ihn zurückzusetzten");
        lore.add("");
        lore.add("§4FEHLERHAFT: §7Nutze §b/fix §7alternativ");
        inventoryManager.addItemStack(inventory, new ItemStack(Material.FLOWER_POT),  31, "§6Disbinder", lore);
        addInformationItem(inventory, teleporter, 13);
        if(player.getUniqueId().equals(teleporter.getOwner())) {
            addDismountItem(inventory, 53);
        }

        return inventory;
    }

    public void openInventory(Player player, Teleporter teleporter) {
        player.openInventory(getInventory(player, teleporter));
    }

    private void addDismountItem(Inventory inventory, int position) {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§cKlick mich, um den");
        lore.add("§cTeleporter abzubauen");


        inventoryManager.addItemStack(inventory, itemStack, position, "§4Teleporter abbauen", lore );
    }

    private void addInformationItem(Inventory inventory, Teleporter teleporter, int position) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Besitzer: §c" + Bukkit.getOfflinePlayer(teleporter.getOwner()).getName());
        lore.add("§7Benutzt: §c" + teleporter.getUsedCounter());
        lore.add("§7Status: " + ((teleporter.isActivated()) ? "§aAktiviert": "§cDeaktiviert"));
        lore.add("");

        inventoryManager.addItemStack(inventory, new ItemStack(Material.TOTEM_OF_UNDYING), position, "§c"+teleporter.getName(), lore );
    }




}
