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

public class TeleportCrystal implements CustomRecipe {

    public TeleportCrystal(SurvivalEngine plugin) {
        plugin.addRecipe(this);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§dTeleport Crystal");
        List<String> lore = new ArrayList<>();
        lore.add("§7This crystal can teleport you!");
        lore.add("§7");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ShapelessRecipe getRecipe () {
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(NamespacedKey.minecraft("teleport_crystal"), getItem());
        shapelessRecipe.addIngredient(1, Material.AMETHYST_SHARD);
        shapelessRecipe.addIngredient(1, Material.ENDER_EYE);
        shapelessRecipe.addIngredient(2, Material.GLOW_INK_SAC);

        return shapelessRecipe;
    }

}
