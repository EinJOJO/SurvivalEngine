package me.einjojo.survivalengine.recipe;


import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract class CustomRecipe {

    private final NamespacedKey namespace;

    protected CustomRecipe(NamespacedKey namespace) {
        this.namespace = namespace;
        SurvivalEngine.getInstance().getRecipeManager().addRecipe(this);
    }

    public NamespacedKey getNamespace() {
        return namespace;
    }

    public abstract ItemStack getItem();
    public abstract Recipe getRecipe();

}
