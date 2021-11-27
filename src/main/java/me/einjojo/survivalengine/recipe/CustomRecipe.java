package me.einjojo.survivalengine.recipe;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface CustomRecipe {
    ItemStack getItem();
    Recipe getRecipe();

}
