package me.einjojo.survivalengine.tabcomplete;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.manager.TeamManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamTabComplete implements TabCompleter {

    private final PlayerManager playerManager;
    private final TeamManager teamManager;

    public TeamTabComplete(SurvivalEngine plugin) {
        playerManager = plugin.getPlayerManager();
        teamManager = plugin.getTeamManager();
        plugin.getCommand("team").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();

        if(!(sender instanceof Player)) {
            return arrayList;
        }
        Player player = (Player) sender;
        SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);

        if(args.length == 1) {
            Team team = survivalPlayer.getTeam();
            if(team == null) {
                arrayList.add("create");
                arrayList.add("join");
                return arrayList;
            } else {
                if(team.isOwner(player.getUniqueId())) {
                    arrayList.add("delete");
                    arrayList.add("invite");
                    arrayList.add("kick");
                }
            }
            arrayList.add("leave");
            arrayList.add("setbase");
            arrayList.add("info");
        } else if (args.length == 2) {
            Team team = survivalPlayer.getTeam();
            if(team == null) return arrayList;
            if(team.isOwner(player.getUniqueId())) {
                if(alias.toLowerCase().endsWith("kick")) {
                    for (UUID member : team.getMembers()) {
                        arrayList.add(Bukkit.getOfflinePlayer(member).getName());
                    }
                } else if(alias.toLowerCase().endsWith("invite")) {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        arrayList.add(target.getName());
                    }
                }
            }
        }
        return arrayList;
    }
}
