package me.einjojo.survivalengine.object;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SurvivalPlayer implements ConfigurationSerializable {

    private final UUID uuid;
    private final PlayerStats statistics;
    private boolean scoreboardActivated;


    public SurvivalPlayer(UUID uuid, boolean scoreboardActivated, PlayerStats playerStats) {
        this.uuid = uuid;
        this.scoreboardActivated = scoreboardActivated;
        this.statistics = playerStats;
    }

    public SurvivalPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.scoreboardActivated = true;
        this.statistics = new PlayerStats(player.getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer () {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public String getName() {
        return getOfflinePlayer().getName();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Team getTeam() {
        return SurvivalEngine.getInstance().getTeamManager().getTeamByPlayer(uuid);
    }

    public void leaveTeam() {
        SurvivalEngine.getInstance().getTeamManager().removePlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setScoreboardActivated(boolean scoreboardActivated) {
        this.scoreboardActivated = scoreboardActivated;
    }

    public void setTeam(Team team) {
        SurvivalEngine.getInstance().getTeamManager().addPlayerToTeam(uuid, team);
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

        players.put("scoreboard", isScoreboardActivated());
        players.put("stats", getStatistics().serialize());

        return players;
    }
}
