package me.einjojo.survivalengine.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Team implements ConfigurationSerializable {

    private final UUID id;
    private final List<UUID> members;


    private String name;
    private UUID owner;
    private Location baseLocation;


    public Team(String name, UUID owner)
    {
        this.id = UUID.randomUUID();
        this.name = name;
        this.owner = owner;
        this.members = new ArrayList<>();
        this.baseLocation = null;
    }

    public Team(UUID id, List<UUID> members, String name, UUID owner, Location baseLocation) {
        this.id = id;
        this.members = members;
        this.name = name;
        this.owner = owner;
        this.baseLocation = baseLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }



    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setBaseLocation(Location baseLocation) {
        this.baseLocation = baseLocation;
    }

    public String getName() {
        return name;
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", getName());
        map.put("base", getBaseLocation().serialize());
        map.put("owner", getOwner().toString());
        map.put("members", getMembers());

        return map;
    }
}
