package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import me.einjojo.survivalengine.object.Team;
import me.einjojo.survivalengine.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    SurvivalEngine plugin;

    public PlayerChatListener(SurvivalEngine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if(!e.isCancelled()) {
            e.setCancelled(true);
            SurvivalPlayer survivalPlayer = plugin.getPlayerManager().getPlayer(e.getPlayer());
            String message = e.getMessage();
            String playerName = e.getPlayer().getName();
            if(survivalPlayer.getTeam() == null) {
                Bukkit.broadcastMessage(String.format("§7%s §8» §7%s", playerName, message));
            } else {
                Team team = survivalPlayer.getTeam();
                if(survivalPlayer.isTeamChat()) {
                    team.chat(TextUtil.toTeamChat(playerName, message));
                } else {
                    Bukkit.broadcastMessage(String.format("§7[§e%s§7] §7%s §8» §7%s", team.getName(), playerName, message));
                }
            }
        }
    }
}
