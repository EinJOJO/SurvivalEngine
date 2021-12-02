package me.einjojo.survivalengine.recipe;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleportCrystalRecipe extends CustomRecipe {

    private static ItemStack itemStack;

    public TeleportCrystalRecipe(SurvivalEngine plugin) {
        super(NamespacedKey.minecraft("teleport_crystal"));
        plugin.recipeManager.addRecipe(this);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§dTeleport Kristall");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Einmal an einen §5Teleporter§7,");
        lore.add("§7gebunden kannst du dich");
        lore.add("§7jederzeit zu ihm teleportieren");
        lore.add("");
        lore.add("§cVerlinke mich an einen Teleporter");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        itemStack = item;
        return item;
    }

    public static ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ShapelessRecipe getRecipe () {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(getNamespace(), getItem());
        shapelessRecipe.addIngredient(1, Material.AMETHYST_SHARD);
        shapelessRecipe.addIngredient(1, Material.ENDER_PEARL);
        shapelessRecipe.addIngredient(2, Material.GLOWSTONE_DUST);

        return shapelessRecipe;
    }

}
