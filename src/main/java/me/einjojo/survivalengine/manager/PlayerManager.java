package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.PlayerStats;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.config.PlayerConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {


    private final SurvivalEngine plugin;
    private final PlayerConfig config;
    private final Map<UUID, SurvivalPlayer> players;

    public PlayerManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new PlayerConfig(plugin);
        this.players = new HashMap<>();
    }

    public List<SurvivalPlayer> getPlayers() {
        List<SurvivalPlayer> survivalPlayers = new ArrayList<>();
        for(Map.Entry<UUID, SurvivalPlayer> entry: players.entrySet()) {
            survivalPlayers.add(entry.getValue());
        }
        return survivalPlayers;
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

    public SurvivalPlayer getPlayer(String name) {
        for (Map.Entry<UUID, SurvivalPlayer> entry : players.entrySet()) {
            if(name.equals(entry.getValue().getName())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public SurvivalPlayer getPlayer(UUID uuid) {
        return this.players.get(uuid);
    }

    public SurvivalPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
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
            boolean hasScoreboard = (configurationSection.isBoolean(player + ".scoreboard") && configurationSection.getBoolean(player + ".scoreboard"));
            PlayerStats playerStats = loadStats(configurationSection.getConfigurationSection(player + ".stats"));
            List<String> rewards = configurationSection.getStringList(player + ".rewards");
            boolean teamChat = configurationSection.getBoolean(player + ".teamchat");

            createPlayer(uuid, new SurvivalPlayer(uuid, hasScoreboard, playerStats, rewards, teamChat));
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

