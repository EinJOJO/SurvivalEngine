package me.einjojo.survivalengine;

import me.einjojo.survivalengine.listener.PlayerDeathListener;
import me.einjojo.survivalengine.listener.PlayerJoinListener;
import me.einjojo.survivalengine.listener.PlayerQuitListener;
import me.einjojo.survivalengine.recipe.CustomRecipe;
import me.einjojo.survivalengine.recipe.TeleportCrystal;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    @Override
    public void onEnable() {
        registerListeners();
        registerRecipes();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPREFIX() {
        return this.PREFIX;
    }

    private void registerRecipes() {
        new TeleportCrystal(this);
    }

    public void addRecipe(CustomRecipe customRecipe) {
        getServer().addRecipe(customRecipe.getRecipe());
        getLogger().log(Level.INFO, "Registered new Recipe");

    }

    private void registerListeners( ) {
        new PlayerDeathListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }
}
