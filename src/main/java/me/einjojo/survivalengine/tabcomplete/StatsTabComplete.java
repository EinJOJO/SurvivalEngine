package me.einjojo.survivalengine.tabcomplete;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class StatsTabComplete implements TabCompleter {


    private final PlayerManager playerManager;

    public StatsTabComplete(SurvivalEngine plugin) {
        this.playerManager = plugin.getPlayerManager();
        plugin.getCommand("stats").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arraylist = new ArrayList<>();

        if(args.length == 1) {
            playerManager.getPlayers().forEach((player) -> {
                arraylist.add(player.getName());
            });
        }

        return arraylist;
    }
}
