package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.DifficultyCommand;
import me.einjojo.survivalengine.command.GetCommand;
import me.einjojo.survivalengine.listener.*;
import me.einjojo.survivalengine.manager.InventoryManager;
import me.einjojo.survivalengine.manager.RecipeManager;
import me.einjojo.survivalengine.manager.TeleportManager;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.manager.TabListManager;
import me.einjojo.survivalengine.util.config.TeleporterConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    public RecipeManager recipeManager;
    public TabListManager tabListManager;
    private TeleportManager teleportManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        initClasses();
        registerListeners();
        registerRecipes();
        registerCommands();

        teleportManager.load();
    }

    @Override
    public void onDisable() {
        teleportManager.save();


    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public String getPREFIX() {
        return this.PREFIX;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    private void registerRecipes() {
        new TeleportCrystalRecipe(this);
        new TeleporterRecipe(this);
    }

    private void initClasses() {
        this.recipeManager = new RecipeManager(this);
        this.tabListManager = new TabListManager();
        this.teleportManager = new TeleportManager(this);
        this.inventoryManager = new InventoryManager();
    }


    private void registerCommands() {
        new DifficultyCommand(this);
        new GetCommand(this);
    }

    private void registerListeners( ) {
        //new EntityPlaceListener(this);
        new TeleporterPlaceListener(this);
        new PlayerInteractListener(this);
        new PlayerDeathListener(this);
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
        new PlayerInteractAtEntityListener(this);
        new EntityExplodeListener(this);
        new InventoryClickListener(this);
    }
}
