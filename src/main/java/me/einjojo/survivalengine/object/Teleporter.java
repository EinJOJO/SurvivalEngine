package me.einjojo.survivalengine.object;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Teleporter implements ConfigurationSerializable {

    private final String name;
    private final Location location;
    private int usedCounter;
    private boolean activated;
    private final UUID owner;
    private final List<String> LINKED_TELEPORTER;
    private Type type;

    public Teleporter(String name, Location location, int usedCounter, boolean activated, UUID owner, List<String> linkedTeleporter, Type type) {
        this.name = name;
        this.location = location;
        this.usedCounter = usedCounter;
        this.activated = activated;
        this.owner = owner;
        this.LINKED_TELEPORTER = linkedTeleporter;
        this.type = type;

    }

    public Teleporter(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.usedCounter = 0;
        this.activated = true;
        this.owner = owner;
        this.LINKED_TELEPORTER = new ArrayList<>();
        this.type = Type.PLAYER;
    }

    public void link(Teleporter teleporter) throws Exception {
        link(teleporter.getName());
    }

    public void unLink(Teleporter teleporter) {
        unLink(teleporter.getName());
    }

    public void unLink(String teleporterName) {
        this.getLINKED_TELEPORTER().remove(teleporterName);
    }

    public void link(String teleporterName) throws Exception {

        if(teleporterName.equals(getName())) {
            throw new Exception("Teleporter kann nicht mit sich selbst verbunden werden.");
        }

        if(!this.LINKED_TELEPORTER.contains(teleporterName)) {
            this.LINKED_TELEPORTER.add(teleporterName);
        } else {
            throw new Exception("Der Teleporter ist bereits verbunden.");
        }
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
    public Type getType() {
        return type;
    }

    public boolean isOwner(Player player) {
        return getOwner().equals(player.getUniqueId());
    }

    public boolean isOwner(Team team) {
        return getOwner().equals(team.getId());
    }

    public List<String> getLINKED_TELEPORTER() {
        return LINKED_TELEPORTER;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> teleporter = new HashMap<>();

        teleporter.put("owner", getOwner().toString());
        teleporter.put("location", String.format("%s %d %d %d", getLocation().getWorld().getWorldFolder().getName(), new Double(location.getX()).intValue() , new Double(location.getY()).intValue(), new Double(location.getZ()).intValue()));
        teleporter.put("used", getUsedCounter());
        teleporter.put("activated", isActivated());
        teleporter.put("linked", getLINKED_TELEPORTER());
        teleporter.put("type", getType().toString());


        return teleporter;

    }

    public String getName() {
        return name;
    }


    public enum Type {
        TEAM("team"),
        PLAYER("player");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }
    }
}

