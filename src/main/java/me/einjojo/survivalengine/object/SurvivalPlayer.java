package me.einjojo.survivalengine.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SurvivalPlayer implements ConfigurationSerializable {

    private final Player player;

    private Team team;
    private boolean scoreboard;


    public SurvivalPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> players = new HashMap<>();

        players.put("team", team.getId());
        players.put("scoreboard", scoreboard);

        return players;
    }
}
