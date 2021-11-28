package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.DifficultyCommand;
import me.einjojo.survivalengine.listener.BlockPlaceListener;
import me.einjojo.survivalengine.listener.PlayerDeathListener;
import me.einjojo.survivalengine.listener.PlayerJoinListener;
import me.einjojo.survivalengine.listener.PlayerQuitListener;
import me.einjojo.survivalengine.recipe.CustomRecipe;
import me.einjojo.survivalengine.recipe.RecipeManager;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.tablist.TabListManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    public RecipeManager recipeManager;
    public TabListManager tabListManager;

    @Override
    public void onEnable() {
        initClasses();
        registerListeners();
        registerRecipes();
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPREFIX() {
        return this.PREFIX;
    }

    private void registerRecipes() {
        new TeleportCrystalRecipe(this);
        new TeleporterRecipe(this);
    }

    private void initClasses() {
        this.recipeManager = new RecipeManager(this);
        this.tabListManager = new TabListManager();
    }


    private void registerCommands() {
        new DifficultyCommand(this);
    }

    private void registerListeners( ) {
        new BlockPlaceListener(this);
        new PlayerDeathListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }
}
