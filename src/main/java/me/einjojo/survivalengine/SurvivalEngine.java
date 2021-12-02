package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.DifficultyCommand;
import me.einjojo.survivalengine.command.GetCommand;
import me.einjojo.survivalengine.listener.*;
import me.einjojo.survivalengine.manager.*;
import me.einjojo.survivalengine.object.PlayerStats;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.config.TeleporterConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    public RecipeManager recipeManager;
    public TabListManager tabListManager;
    private TeleportManager teleportManager;
    private InventoryManager inventoryManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        initClasses();
        registerListeners();
        registerRecipes();
        registerCommands();

        ConfigurationSerialization.registerClass(PlayerStats.class);
        teleportManager.load();
        playerManager.load();
    }

    @Override
    public void onDisable() {
        teleportManager.save();
        playerManager.save();
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
        this.playerManager = new PlayerManager(this);
    }


    private void registerCommands() {
        new DifficultyCommand(this);
        new GetCommand(this);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
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
        new StatsListener(this);
    }
}
