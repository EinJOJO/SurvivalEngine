package me.einjojo.survivalengine.object;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats implements ConfigurationSerializable {

    private final UUID uuid;
    private int playerKills;
    private int mobKills;
    private int blocksPlaced;
    private int blocksDestroyed;
    private int deaths;
    private final long firstJoin;

    public PlayerStats(UUID uuid, int playerKills, int mobKills, int blocksPlaced, int blocksDestroyed, long firstJoin, int deaths) {
        this.uuid = uuid;
        this.playerKills = playerKills;
        this.mobKills = mobKills;
        this.blocksPlaced = blocksPlaced;

        this.blocksDestroyed = blocksDestroyed;
        this.firstJoin = firstJoin;
        this.deaths = deaths;
    }

    public PlayerStats(UUID uuid) {
        this.uuid = uuid;
        this.playerKills = 0;
        this.mobKills = 0;
        this.blocksPlaced = 0;
        this.blocksDestroyed = 0;
        this.deaths = 0;
        this.firstJoin = System.currentTimeMillis();
    }

    public int getPlayerKills() {
        return playerKills;
    }

    public void setPlayerKills(int playerKills) {
        this.playerKills = playerKills;
    }

    public int getMobKills() {
        return mobKills;
    }

    public void setMobKills(int mobKills) {
        this.mobKills = mobKills;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public int getBlocksDestroyed() {
        return blocksDestroyed;
    }

    public void setBlocksDestroyed(int blocksDestroyed) {
        this.blocksDestroyed = blocksDestroyed;
    }

    public long getFirstJoin() {
        return firstJoin;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeaths(int deaths) {
        setDeaths(getDeaths() + deaths);
    }

    public void addBocksPlaced(int blocks) {
        setBlocksPlaced(getBlocksPlaced() + blocks);
    }

    public void addBocksDestroyed(int blocks) {
        setBlocksDestroyed(getBlocksDestroyed() + blocks);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("playerKills", getPlayerKills());
        map.put("mobKills", getMobKills());
        map.put("blocksPlaced", getBlocksPlaced());
        map.put("blocksDestroyed", getBlocksDestroyed());
        map.put("firstJoin", getFirstJoin());
        map.put("deaths", getDeaths());

        return map;
    }
}
