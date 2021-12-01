package me.einjojo.survivalengine.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryManager {


    public void addItemStack(Inventory inventory, ItemStack itemStack, int position, String displayName, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        addItemStack(inventory, itemStack, position);
    }
    public void addItemStack(Inventory inventory, ItemStack itemStack, int position, String displayName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        addItemStack(inventory, itemStack, position);
    }
    public void addItemStack(Inventory inventory, ItemStack itemStack, int position) {
        inventory.setItem(position, itemStack);
    }

    public void fillGuiWithGlass(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            addItemStack(inventory, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), i, "§7");
        }
    }

}
