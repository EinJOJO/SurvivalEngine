package me.einjojo.survivalengine.command;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.manager.PlayerManager;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.util.TextUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    private final String PREFIX;
    private final PlayerManager playerManager;
    public StatsCommand(SurvivalEngine plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.PREFIX = plugin.getPREFIX();
        plugin.getCommand("stats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            SurvivalPlayer survivalPlayer = playerManager.getPlayer(player);
            player.spigot().sendMessage(getPlayerStats(survivalPlayer));
        } else if (args.length == 1) {
            SurvivalPlayer survivalPlayer = playerManager.getPlayer(args[0]);
            if(survivalPlayer == null) {
                player.sendMessage(PREFIX + "§cDieser Spieler existiert nicht.");
                return true;
            }
            player.spigot().sendMessage(getPlayerStats(survivalPlayer));
        } else {
            sendUsage(player);
        }

        return false;
    }

    private void sendUsage(Player player) {
        player.sendMessage(PREFIX + "§7Nutze: §b/stats [Spieler]");
    }

    private TextComponent getPlayerStats(SurvivalPlayer player) {
        TextComponent line0 = new TextComponent("§7\n");
        TextComponent line1 = new TextComponent(PREFIX + "§fStatistiken von §e" + player.getName() + "\n");
        TextComponent line2 = new TextComponent(PREFIX + String.format("§fSpieler Kills: §b%d  \n", player.getStatistics().getPlayerKills()));
        TextComponent line3 = new TextComponent(PREFIX + String.format("§fMob Kills: §b%d  \n", player.getStatistics().getMobKills()));
        TextComponent line3_1 = new TextComponent(PREFIX + String.format("§fSchaden gemacht: §b%d \n", player.getOfflinePlayer().getStatistic(Statistic.DAMAGE_DEALT)));
        TextComponent line4 = new TextComponent(PREFIX + String.format("§fTode : §b%d  \n", player.getStatistics().getDeaths()));
        TextComponent line5 = new TextComponent(PREFIX + String.format("§fBlöcke abgebaut: §b%d  \n", player.getStatistics().getBlocksDestroyed()));
        TextComponent line6 = new TextComponent(PREFIX + String.format("§fBlöcke platziert: §b%d  \n", player.getStatistics().getBlocksPlaced()));
        TextComponent line7 = new TextComponent("§7\n");
        TextComponent line8 = new TextComponent(PREFIX + String.format("§fZuletzt gestorben vor: §b%s \n", TextUtil.getTimeString(player.getOfflinePlayer().getStatistic(Statistic.TIME_SINCE_DEATH))));
        TextComponent line9 = new TextComponent(PREFIX + String.format("§fSpielzeit: §b%s \n", TextUtil.getTimeString(player.getOfflinePlayer().getStatistic(Statistic.TOTAL_WORLD_TIME))));
        TextComponent line10 = new TextComponent("§7\n");

        line9.addExtra(line10);
        line8.addExtra(line9);
        line7.addExtra(line8);
        line6.addExtra(line7);
        line5.addExtra(line6);
        line4.addExtra(line5);
        line3_1.addExtra(line4);
        line3.addExtra(line3_1);
        line2.addExtra(line3);
        line1.addExtra(line2);
        line0.addExtra(line1);

        return line0;
    }


}
