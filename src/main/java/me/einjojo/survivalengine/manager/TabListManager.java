package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TabListManager {

    private final TeamManager teamManager;
    private final PlayerManager playerManager;
    private final Map<UUID, String> TEAM_MAP;
    private Scoreboard scoreboard;

    public TabListManager(SurvivalEngine plugin) {
        this.teamManager = plugin.getTeamManager();
        this.playerManager = plugin.getPlayerManager();
        this.TEAM_MAP = new HashMap<>();
    }

    public void setPlayerList(Player player) {
        player.setPlayerListHeaderFooter("§b§lSURVIVAL V2\n","\n§7Powered by the §fSurvivalEngine§oV2 \n " +
                "§3/difficulty /team /");
    }


    public void registerTeam(Player player) {
        this.scoreboard = player.getScoreboard();
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
        me.einjojo.survivalengine.object.Team playerTeam = survivalPlayer.getTeam();

        String s = "";

        if(playerTeam != null) {
            s += playerTeam.getName().substring(1,4);
        }
        s += player.getUniqueId().toString().substring(1,6);

        if(scoreboard.getTeam(s) != null) {
            scoreboard.getTeam(s).unregister();
        }

        Team team = scoreboard.registerNewTeam(s);
        if (playerTeam != null) {
            team.setPrefix("§7[§e"+ playerTeam.getName() + "§7] ");
        }
        team.setColor(ChatColor.GRAY);
        this.TEAM_MAP.put(player.getUniqueId(), s);
        update();
    }


    public void update(){
        for (Player target: Bukkit.getOnlinePlayers()) {
            UUID targetUUID = target.getUniqueId();
            if(!scoreboard.getTeam(TEAM_MAP.get(targetUUID)).getEntries().contains(target.getName())) {
                scoreboard.getTeam(TEAM_MAP.get(targetUUID)).addEntry(target.getName());
            }
        }
    }

}
