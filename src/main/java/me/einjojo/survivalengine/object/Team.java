package me.einjojo.survivalengine.object;

import me.einjojo.survivalengine.SurvivalEngine;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.*;

public class Team implements ConfigurationSerializable {

    private final UUID id;
    private final List<UUID> members;
    private final List<UUID> invites;


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
        this.invites = new ArrayList<>();
        members.add(owner);
    }

    public Team(UUID id, List<UUID> members, String name, UUID owner, Location baseLocation, List<UUID> invites) {
        this.id = id;
        this.members = members;
        this.name = name;
        this.owner = owner;
        this.baseLocation = baseLocation;
        this.invites = invites;
    }

    public List<Teleporter> getTeleporter() {
        return SurvivalEngine.getInstance().getTeleportManager().getTeleporterByTeam(this.id);
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

    public boolean isMember(UUID player) {
        return getMembers().contains(player);
    }

    public boolean isMember(Player player) {
        return isMember(player.getUniqueId());
    }
    public List<UUID> getMembers() {
        return members;
    }

    public boolean isOwner(UUID uuid) {
        return getOwner().equals(uuid);
    }
    public boolean isOwner(Player player) { return isOwner(player.getUniqueId()); }

    public List<UUID> getInvites() {
        return invites;
    }

    public void invite(UUID player) {
        if(!getInvites().contains(player)) {
            getInvites().add(player);
        }
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public boolean isInvited(UUID player) {
        return getInvites().contains(player);
    }

    public void sendMessage(String message) {
        getMembers().forEach((memberUUID) -> {
            Player target = Bukkit.getPlayer(memberUUID);
            if(target != null) {
                target.sendMessage(message);
            }
        });
    }

    public void sendMessage(TextComponent textComponent) {
        getMembers().forEach((memberUUID) -> {
            Player target = Bukkit.getPlayer(memberUUID);
            if(target != null) {
                target.spigot().sendMessage(textComponent);
            }
        });
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        List<String> memberString = new ArrayList<>();
        List<String> inviteString = new ArrayList<>();
        getMembers().forEach((member)->{
            memberString.add(member.toString());
        });
        getInvites().forEach((invited)->{
            inviteString.add(invited.toString());
        });
        map.put("name", getName());
        if(getBaseLocation() != null) {
            Location location = getBaseLocation();
            map.put("base", String.format("%s %d %d %d", location.getWorld().getWorldFolder().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ() ));
        } else {
            map.put("base", "null");
        }
        map.put("owner", getOwner().toString());
        map.put("members", memberString);
        map.put("invites", inviteString);

        return map;
    }
}
