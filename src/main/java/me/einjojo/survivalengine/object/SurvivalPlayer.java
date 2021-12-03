package me.einjojo.survivalengine.object;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class SurvivalPlayer implements ConfigurationSerializable {

    private final UUID uuid;
    private final PlayerStats statistics;
    private boolean scoreboardActivated;
    private final List<String> rewards;


    public SurvivalPlayer(UUID uuid, boolean scoreboardActivated, PlayerStats playerStats, List<String> rewards) {
        this.uuid = uuid;
        this.scoreboardActivated = scoreboardActivated;
        this.statistics = playerStats;
        this.rewards = rewards;
    }

    public SurvivalPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.scoreboardActivated = true;
        this.statistics = new PlayerStats(player.getUniqueId());
        this.rewards = new ArrayList<>();
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

    public List<String> getRewards() {
        return rewards;
    }

    public boolean hasReward(String identifier) {
        return getRewards().contains(identifier);
    }

    public void claimReward(String identifier) {
        getRewards().add(identifier);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> players = new HashMap<>();

        players.put("scoreboard", isScoreboardActivated());
        players.put("stats", getStatistics().serialize());
        players.put("rewards", getRewards());

        return players;
    }
}
