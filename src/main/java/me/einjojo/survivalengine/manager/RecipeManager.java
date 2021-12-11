package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.recipe.ChickenControllerRecipe;
import me.einjojo.survivalengine.recipe.CustomRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;

public class RecipeManager {

    private final SurvivalEngine plugin;

    private final ArrayList<NamespacedKey> recipes = new ArrayList<>();

    public RecipeManager(SurvivalEngine plugin) {
        this.plugin = plugin;
    }


    public void addRecipe(CustomRecipe customRecipe) {
        plugin.getServer().addRecipe(customRecipe.getRecipe());
        recipes.add(customRecipe.getNamespace());
    }

    public void loadRecipes(Player player) {
        Set<NamespacedKey> discovered = player.getDiscoveredRecipes();
        for (NamespacedKey recipe: recipes) {
            if(!discovered.contains(recipe)) {
                player.discoverRecipe(recipe);
            }
        }
    }

}
