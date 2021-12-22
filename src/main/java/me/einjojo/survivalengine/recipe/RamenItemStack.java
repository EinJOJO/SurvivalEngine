package me.einjojo.survivalengine.recipe;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RamenItemStack {

    public static ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.RABBIT_STEW, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName("§6Ramen");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§cRamen aus dem");
        lore.add("§cKonoha Gakure");
        lore.add("");
        itemMeta.setLore(lore);


        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
