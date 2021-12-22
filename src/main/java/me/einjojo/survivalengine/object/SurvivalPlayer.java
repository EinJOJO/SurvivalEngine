package me.einjojo.survivalengine.object;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class SurvivalPlayer implements ConfigurationSerializable {

    private final UUID uuid;
    private final PlayerStats statistics;
    private boolean scoreboardActivated;
    private final List<String> rewards;
    private boolean teamChat;
    private BossBar bossBar;
    private boolean resetSpawn;


    public SurvivalPlayer(UUID uuid, boolean scoreboardActivated, PlayerStats playerStats, List<String> rewards, boolean teamChat, boolean resetSpawn) {
        this.uuid = uuid;
        this.scoreboardActivated = scoreboardActivated;
        this.statistics = playerStats;
        this.rewards = rewards;
        this.teamChat = teamChat;
        this.resetSpawn = resetSpawn;
    }

    public SurvivalPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.scoreboardActivated = true;
        this.statistics = new PlayerStats(player.getUniqueId());
        this.rewards = new ArrayList<>();
        this.teamChat = false;
        this.resetSpawn = false;
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

    public boolean isTeamChat() {
        return teamChat;
    }

    public void setTeamChat(boolean teamChat) {
        this.teamChat = teamChat;
    }

    public List<Teleporter> getTeleporter() {
        return SurvivalEngine.getInstance().getTeleportManager().getTeleporterByPlayer(uuid);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public void setResetSpawn(boolean resetSpawn) {
        this.resetSpawn = resetSpawn;
    }

    public boolean isResetSpawn() {
        return resetSpawn;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> players = new HashMap<>();

        players.put("scoreboard", isScoreboardActivated());
        players.put("stats", getStatistics().serialize());
        players.put("rewards", getRewards());
        players.put("teamchat", isTeamChat());
        players.put("resetSpawn", isResetSpawn());

        return players;
    }
}
