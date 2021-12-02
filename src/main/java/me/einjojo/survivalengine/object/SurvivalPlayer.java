package me.einjojo.survivalengine.object;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SurvivalPlayer implements ConfigurationSerializable {

    private final UUID uuid;
    private final PlayerStats statistics;
    private int teamID;
    private boolean scoreboardActivated;


    public SurvivalPlayer(UUID uuid, int teamID, boolean scoreboardActivated, PlayerStats playerStats) {
        this.uuid = uuid;
        this.scoreboardActivated = scoreboardActivated;
        this.teamID = teamID;
        this.statistics = playerStats;
    }

    public SurvivalPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.teamID = -1;
        this.scoreboardActivated = true;
        this.statistics = new PlayerStats(player.getUniqueId());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public int getTeamID() {
        return teamID;
    }

    public void setScoreboardActivated(boolean scoreboardActivated) {
        this.scoreboardActivated = scoreboardActivated;
    }

    public void setTeam(int team) {
        this.teamID = team;
    }

    public boolean isScoreboardActivated() {
        return scoreboardActivated;
    }

    public PlayerStats getStatistics() {
        return statistics;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> players = new HashMap<>();

        players.put("team", teamID);
        players.put("scoreboard", isScoreboardActivated());
        players.put("stats", getStatistics().serialize());

        return players;
    }

    public static void deserialize(Map<String, Object> map) {

    }
}
