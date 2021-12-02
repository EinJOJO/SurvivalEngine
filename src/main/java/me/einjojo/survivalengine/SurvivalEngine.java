package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.DifficultyCommand;
import me.einjojo.survivalengine.command.GetCommand;
import me.einjojo.survivalengine.command.TeamCommand;
import me.einjojo.survivalengine.listener.*;
import me.einjojo.survivalengine.manager.*;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.tabcomplete.DifficultyTabComplete;
import me.einjojo.survivalengine.tabcomplete.TeamTabComplete;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    private static SurvivalEngine instance;
    public RecipeManager recipeManager;
    public TabListManager tabListManager;
    private TeleportManager teleportManager;
    private InventoryManager inventoryManager;
    private PlayerManager playerManager;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        initClasses();
        registerRecipes();
        registerCommands();
        registerListeners();
        registerTabComplete();
        teleportManager.load();
        playerManager.load();
        teamManager.load();
    }

    @Override
    public void onDisable() {
        this.teleportManager.save();
        this.playerManager.save();
        this.teamManager.save();
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
        instance = this;
        this.recipeManager = new RecipeManager(this);
        this.tabListManager = new TabListManager();
        this.teleportManager = new TeleportManager(this);
        this.inventoryManager = new InventoryManager();
        this.playerManager = new PlayerManager(this);
        this.teamManager = new TeamManager(this);
    }


    private void registerCommands() {
        new DifficultyCommand(this);
        new TeamCommand(this);
        new GetCommand(this);
    }

    private void registerTabComplete() {
        new DifficultyTabComplete(this);
        new TeamTabComplete(this);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    private void registerListeners() {
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

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static SurvivalEngine getInstance() {
        return instance;
    }
}
