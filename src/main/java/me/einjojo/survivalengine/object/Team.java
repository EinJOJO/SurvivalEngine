package me.einjojo.survivalengine.object;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Team implements ConfigurationSerializable {

    private int id;
    private String name;
    private Player Owner;
    private SurvivalPlayer[] members;
    private Location baseLocation;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public Player getOwner() {
        return Owner;
    }

    public SurvivalPlayer[] getMembers() {
        return members;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", getName());
        map.put("baseLocation", getBaseLocation().serialize());
        map.put("owner", getOwner());
        map.put("members", getMembers());

        return map;
    }
}
