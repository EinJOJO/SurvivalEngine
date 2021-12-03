package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.config.TeamConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private final Map<UUID, Team> TEAM_MAP;
    private final Map<UUID, Team> PLAYER_MAP;
    private final SurvivalEngine plugin;
    private final TeamConfig config;

    public TeamManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new TeamConfig(plugin);
        this.TEAM_MAP = new HashMap<>();
        this.PLAYER_MAP = new HashMap<>();
    }

    public boolean createTeam(Team team) {
        if(!TEAM_MAP.containsKey(team.getId())) {
            TEAM_MAP.put(team.getId(), team);
            for (UUID pUuid : team.getMembers()) {
                PLAYER_MAP.put(pUuid, team);
            }
            return true;
        }
        return false;
    }

    public void addPlayerToTeam(Player player, Team team) {
        addPlayerToTeam(player.getUniqueId(), team);
    }

    public void addPlayerToTeam(UUID pUuid, Team team) {
        if(PLAYER_MAP.containsKey(pUuid)) {
            PLAYER_MAP.remove(pUuid);
            addPlayerToTeam(pUuid, team);
        } else {
            team.getMembers().add(pUuid);
            PLAYER_MAP.put(pUuid, team);
        }
    }

    public void removePlayer(UUID pUuid) {
        if(PLAYER_MAP.containsKey(pUuid)) {
            PLAYER_MAP.get(pUuid).getMembers().remove(pUuid);
            PLAYER_MAP.remove(pUuid);
        }
    }

    public Team getTeam(UUID uuid) {
        return TEAM_MAP.get(uuid);
    }

    public void deleteTeam(Team team) {
        if(TEAM_MAP.containsKey(team.getId())) {
            team.getMembers().forEach((member)->{
                PLAYER_MAP.remove(member);
            });
            TEAM_MAP.remove(team.getId());
        }
    }

    public Team getTeamByPlayer(UUID uuid) {
        return PLAYER_MAP.get(uuid);
    }

    public Team getTeamByPlayer(Player player) {
        return getTeamByPlayer(player.getUniqueId());
    }

    public void save() {
        config.getFile().set("team", null);
        TEAM_MAP.forEach(config::saveTeam);
    }


    public void load() {
        FileConfiguration configuration = config.getFile();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("team");
        if(configurationSection == null) return;

        Set<String> teamSet = configurationSection.getKeys(false);

        teamSet.forEach((team)->{
            String teamName = configurationSection.getString(team + ".name");
            UUID owner = UUID.fromString(configurationSection.getString(team + ".owner"));
            String baseString = configurationSection.getString(team + ".base");
            List<String> membersString =  configurationSection.getStringList(team + ".members");
            List<String> invitesString =  configurationSection.getStringList(team + ".invites");
            Location baseLocation;
            if(baseString.equals("null")) {
                baseLocation = null;
            } else {
                String[] baseArr = baseString.split(" ");
                 baseLocation = new Location(Bukkit.getWorld(baseArr[0]), Double.parseDouble(baseArr[1]), Double.parseDouble(baseArr[2]), Double.parseDouble(baseArr[3]));
            }

            List<UUID> members = new ArrayList<>();
            List<UUID> invites = new ArrayList<>();

            membersString.forEach((member)->{
                members.add(UUID.fromString(member));
            });

            invitesString.forEach((invited)->{
                invites.add(UUID.fromString(invited));
            });

            createTeam(new Team(UUID.fromString(team), members, teamName, owner, baseLocation, invites));
        });
        plugin.getLogger().info(String.format("Loaded %d teams and %d team-players", TEAM_MAP.size(), PLAYER_MAP.size()));
    }
}
