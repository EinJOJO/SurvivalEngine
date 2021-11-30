package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockCommand implements CommandExecutor {

    private final SurvivalEngine plugin;

    public BlockCommand (SurvivalEngine plugin) {
        this.plugin = plugin;
        plugin.getCommand("black").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length == 1) {
            plugin.getEventBlacklist().add(strings[0]);
            Bukkit.broadcastMessage("Blocking: " + strings[0]);
        }

        return false;
    }
}
