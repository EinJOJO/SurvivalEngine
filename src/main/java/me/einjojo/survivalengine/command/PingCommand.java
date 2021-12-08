package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private SurvivalEngine plugin;

    public PingCommand(SurvivalEngine plugin) {
        plugin.getCommand("ping").setExecutor(this);

        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage(plugin.getPREFIX() + "§cDieser Spieler ist momentan nicht verfügbar.");
                return true;
            }
            player.sendMessage(plugin.getPREFIX() + String.format("§6%s's §7Spielzeit beträgt: §e%d", target.getName(), target.getPing()));
        } else{
            player.sendMessage(plugin.getPREFIX() + "Dein Ping: §e" + player.getPing());
        }

        return true;
    }
}
