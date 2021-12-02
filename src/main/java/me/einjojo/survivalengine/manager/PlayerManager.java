package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.PlayerStats;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.config.PlayerConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {


    private final SurvivalEngine plugin;
    private final PlayerConfig config;
    private final Map<UUID, SurvivalPlayer> players;

    public PlayerManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new PlayerConfig(plugin);
        this.players = new HashMap<>();
    }


    public void createPlayer(Player player) {
        SurvivalPlayer survivalPlayer = new SurvivalPlayer(player);
        UUID uuid = player.getUniqueId();
        createPlayer(uuid, survivalPlayer);
    }

    private void createPlayer(UUID uuid, SurvivalPlayer player) {
        if(!this.players.containsKey(uuid)) {
            this.players.put(uuid, player);
        }
    }

    public SurvivalPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }


    public void save() {
        players.forEach(config::savePlayer);
    }

    public void load() {
        FileConfiguration fileConfiguration = config.getFile();
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("player");


        if(configurationSection == null) {
            return;
        }

        Set<String> playerSet = configurationSection.getKeys(false);

        playerSet.forEach((player)->{
            UUID uuid = UUID.fromString(player);
            int teamID = configurationSection.getInt(player + ".team");
            boolean hasScoreboard = (configurationSection.isBoolean(player + ".scoreboard") && configurationSection.getBoolean(player + ".scoreboard"));
            PlayerStats playerStats = loadStats(configurationSection.getConfigurationSection(player + ".stats"));

            createPlayer(uuid, new SurvivalPlayer(uuid, teamID, hasScoreboard, playerStats));
        });

        plugin.getLogger().info(String.format("Loaded %d players from players.yml", this.players.size()));
    }

    private PlayerStats loadStats(ConfigurationSection section) {
        UUID uuid = UUID.fromString(section.getString("uuid"));
        int playerKills = section.getInt("playerKills");
        int mobKills = section.getInt("mobKills");
        int blocksDestroyed = section.getInt("blocksDestroyed");
        int blocksPlaced = section.getInt("blocksPlaced");
        int deaths = section.getInt("deaths");
        long firstJoin  = section.getLong("firstJoin");

        return new PlayerStats(uuid, playerKills, mobKills, blocksPlaced, blocksDestroyed, firstJoin, deaths);
    }


}

