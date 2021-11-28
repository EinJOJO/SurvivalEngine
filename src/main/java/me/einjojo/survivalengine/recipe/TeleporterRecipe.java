package me.einjojo.survivalengine.recipe;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TeleporterRecipe extends CustomRecipe {


    private static ItemStack itemStack;

    public TeleporterRecipe (SurvivalEngine plugin) {
        super(NamespacedKey.minecraft("teleporter"));
        plugin.recipeManager.addRecipe(this);
    }

    public static ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.END_CRYSTAL, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§dTeleporter");
        List<String> lore = new ArrayList<>();

        lore.add("§7Place this Teleporter on an");
        lore.add("§5Obsidian §7Block");
        lore.add("");
        lore.add("§cCaution: Dont hit it!");

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);


        itemStack = item;
        return item;
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(getNamespace(), getItem());
        shapedRecipe.shape(
                "GGG",
                "GEG",
                "CNC");

        shapedRecipe.setIngredient('G', Material.GLASS);
        shapedRecipe.setIngredient('E', Material.ENDER_EYE);
        shapedRecipe.setIngredient('C', Material.AMETHYST_SHARD);
        shapedRecipe.setIngredient('N', Material.NETHER_STAR);


        return shapedRecipe;
    }
}
