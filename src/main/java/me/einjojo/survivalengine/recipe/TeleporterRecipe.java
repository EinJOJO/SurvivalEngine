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

    public TeleporterRecipe () {
        super(NamespacedKey.minecraft("teleporter"));
    }

    public static ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.END_CRYSTAL, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§d§lTeleporter");
        List<String> lore = new ArrayList<>();

        lore.add("§7Plaziere mich an einem");
        lore.add("§7sicheren Ort!");
        lore.add("");
        lore.add("§cAchtung: Explosionsgefahr");

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
