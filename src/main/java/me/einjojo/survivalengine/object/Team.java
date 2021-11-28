package me.einjojo.survivalengine.object;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

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

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
