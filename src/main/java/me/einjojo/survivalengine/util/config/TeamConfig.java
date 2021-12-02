package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Team;

import java.util.UUID;

public class TeamConfig extends ConfigFile{

    public TeamConfig(SurvivalEngine plugin) {
        super(plugin, "team.yml");
    }

    public void saveTeam(UUID id, Team team) {
        getFile().set("team." + id.toString(), team.serialize());
        saveFile();
    }
}
