package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Team;

public class TeamConfig extends ConfigFile{

    public TeamConfig(SurvivalEngine plugin) {
        super(plugin, "team.yml");
    }

    public void saveTeam(int id, Team team) {
        getFile().set("team." + id, team.serialize());
    }
}
