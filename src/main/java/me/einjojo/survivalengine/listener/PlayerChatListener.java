package me.einjojo.survivalengine.listener;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            TextComponent senderComponent;
            if(survivalPlayer.getTeam() == null) {
                senderComponent = new TextComponent("§7" + playerName);
            } else {
                senderComponent = new TextComponent(String.format("§7[§e%s§7] %s", survivalPlayer.getTeam().getName(), playerName));
            }
            String timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now().plusHours(1));
            Text messageMeta = new Text(String.format("§7Gesendet um: §e%s", timeFormat));
            senderComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, messageMeta));
            TextComponent messageComponent = new TextComponent(" §8» §7" + message);
            senderComponent.addExtra(messageComponent);

            if(survivalPlayer.isTeamChat() && survivalPlayer.getTeam() != null) {
                senderComponent = new TextComponent("§7[§cTeamchat§7] §e" + playerName);
                senderComponent.addExtra(messageComponent);
                survivalPlayer.getTeam().sendMessage(senderComponent);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(senderComponent);
                }
            }

        }
    }
}
