package me.einjojo.survivalengine.object;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Teleporter implements ConfigurationSerializable {

    private final String name;
    private final Location location;
    private int usedCounter;
    private boolean activated;
    private final UUID owner;

    public Teleporter(String name, Location location, int usedCounter, boolean activated, UUID owner) {
        this.name = name;
        this.location = location;
        this.usedCounter = usedCounter;
        this.activated = activated;
        this.owner = owner;

    }

    public Teleporter(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.usedCounter = 0;
        this.activated = true;
        this.owner = owner;
    }

    public boolean isActivated() {
        return activated;
    }

    public int getUsedCounter() {
        return usedCounter;
    }

    public Location getLocation() {
        return location;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setUsedCounter(int usedCounter) {
        this.usedCounter = usedCounter;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> teleporter = new HashMap<>();

        teleporter.put("owner", getOwner().toString());
        teleporter.put("location", String.format("%s %d %d %d", getLocation().getWorld().getWorldFolder().getName(), new Double(location.getX()).intValue() , new Double(location.getY()).intValue(), new Double(location.getZ()).intValue()));
        teleporter.put("used", getUsedCounter());
        teleporter.put("activated", isActivated());


        return teleporter;

    }

    public String getName() {
        return name;
    }
}
