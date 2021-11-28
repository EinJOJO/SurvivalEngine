package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Team;

public class TeamConfig extends ConfigFile{

    protected TeamConfig(SurvivalEngine plugin) {
        super(plugin, "team.yml");
    }

    public void saveTeam(Team team) {
        getFile().set("team." + team.getId(), team.serialize());
    }
}
