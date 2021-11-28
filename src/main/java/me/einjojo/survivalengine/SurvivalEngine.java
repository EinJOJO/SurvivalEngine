package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.DifficultyCommand;
import me.einjojo.survivalengine.command.GetCommand;
import me.einjojo.survivalengine.listener.EntityPlaceListener;
import me.einjojo.survivalengine.listener.PlayerDeathListener;
import me.einjojo.survivalengine.listener.PlayerJoinListener;
import me.einjojo.survivalengine.listener.PlayerQuitListener;
import me.einjojo.survivalengine.manager.RecipeManager;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.manager.TabListManager;
import org.bukkit.plugin.java.JavaPlugin;

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
        new GetCommand(this);
    }

    private void registerListeners( ) {
        new EntityPlaceListener(this);
        new PlayerDeathListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }
}
