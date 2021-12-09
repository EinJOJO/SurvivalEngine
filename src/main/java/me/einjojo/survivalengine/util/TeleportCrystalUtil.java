package me.einjojo.survivalengine.util;

import me.einjojo.survivalengine.object.Teleporter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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

    /**
     *
     * Get Name of the Teleporter from Crystal.
     *
     * @param itemStack Teleport Crystal
     * @return Name of the teleporter: "§c[name]"
     */
    public String getTeleporterNameFromCrystal(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(lore == null) return null;
        String str = lore.get(4);
        if(str.equals("")) return null;
        return str.substring(15);
    }


    public ItemStack getTeleporterShard(Teleporter teleporter) {
        if(teleporter == null) {
            return null;
        }
        ItemStack itemStack = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemMeta.setDisplayName("§5Teleport Splitter");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§cRechtsklicke §7mit dem §5Kristall");
        lore.add("§7einen Teleporter, damit du dich");
        lore.add("§7von diesem, zu:");
        lore.add("§c" + teleporter.getName());
        lore.add("§7teleportieren kannst.");
        lore.add("");

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public String getTeleporterNameFromShard(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) {
            return null;
        }
        List<String> lore = itemMeta.getLore();
        if(lore == null) {
            return null;
        }
        return lore.get(4);
    }

}
