package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.config.TeamConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TeamManager {

    private final Map<Integer, Team> loadedTeams;
    private final SurvivalEngine plugin;
    private final TeamConfig config;

    public TeamManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new TeamConfig(plugin);
        this.loadedTeams = new HashMap<>();
    }


    public void save() {
        loadedTeams.forEach(config::saveTeam);
    }

    public void load() {
        FileConfiguration configuration = config.getFile();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("team");
        if(configurationSection == null) return;

        Set<String> teamSet = configurationSection.getKeys(false);

        teamSet.forEach((team)->{

        });
    }
}
