package me.einjojo.survivalengine.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabListManager {

    public void setPlayerList(Player player) {
        player.setPlayerListHeaderFooter("§b§lSURVIVAL V2\n","\n§7Powered by the §fSurvivalEngine§oV2 \n " +
                "§3/difficulty");
    }

    public void setPlayerTeams(Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Team team = scoreboard.getTeam("1players");
        if(team == null) {
            scoreboard.registerNewTeam("1players");
            team = scoreboard.getTeam("1players");
        }
        team.setColor(ChatColor.GRAY);
        for (Player target: Bukkit.getOnlinePlayers()) {
            team.addEntry(target.getName());
        }
    }

}
