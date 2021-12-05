package me.einjojo.survivalengine;

import me.einjojo.survivalengine.command.*;
import me.einjojo.survivalengine.listener.*;
import me.einjojo.survivalengine.manager.*;
import me.einjojo.survivalengine.recipe.BedrockPickaxeRecipe;
import me.einjojo.survivalengine.recipe.HasteRecipe;
import me.einjojo.survivalengine.recipe.TeleportCrystalRecipe;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.tabcomplete.DifficultyTabComplete;
import me.einjojo.survivalengine.tabcomplete.StatsTabComplete;
import me.einjojo.survivalengine.tabcomplete.TeamTabComplete;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalEngine extends JavaPlugin {

    private final String PREFIX = "§7「§b§lΣNGINΣ§7」» ";

    private static SurvivalEngine instance;
    private RecipeManager recipeManager;
    private TabListManager tabListManager;
    private TeleportManager teleportManager;
    private InventoryManager inventoryManager;
    private PlayerManager playerManager;
    private TeamManager teamManager;
    private BedManager bedManager;

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
        new TeleportCrystalRecipe();
        new TeleporterRecipe();
        new HasteRecipe();
        new BedrockPickaxeRecipe();
    }

    private void initClasses() {
        instance = this;
        this.recipeManager = new RecipeManager(this);
        this.teleportManager = new TeleportManager(this);
        this.inventoryManager = new InventoryManager();
        this.playerManager = new PlayerManager(this);
        this.teamManager = new TeamManager(this);
        this.bedManager = new BedManager();
        this.tabListManager = new TabListManager(this);
    }


    private void registerCommands() {
        new DifficultyCommand(this);
        new TeamCommand(this);
        //new GetCommand(this);
        new TeleporterCommand(this);
        new StatsCommand(this);
    }

    private void registerTabComplete() {
        new DifficultyTabComplete(this);
        new TeamTabComplete(this);
        new StatsTabComplete(this);
        new FixCommand(this);
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
        new BedListener(this);
        new PlayerChatListener(this);
        new EntityDismountListener(this);
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public BedManager getBedManager() {
        return bedManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public TabListManager getTabListManager() {
        return tabListManager;
    }

    public static SurvivalEngine getInstance() {
        return instance;
    }
}
