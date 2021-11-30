package me.einjojo.survivalengine.util;

import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleportCrystalUtil {


    public ItemStack bind(ItemStack itemStack, Teleporter teleporter) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§cRechtsklicke §7um dich zum");
        lore.add("§7Teleporter zu teleportieren");
        lore.add("");
        lore.add("§7Gebunden an: §c" + teleporter.getName());
        lore.add("§7");

        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public String getTeleporterName(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(lore == null) return null;
        String str = lore.get(4);
        if(str.equals("")) return null;
        return str.substring(15);
    }


    public void isBound(ItemStack itemStack) {

    }
}
