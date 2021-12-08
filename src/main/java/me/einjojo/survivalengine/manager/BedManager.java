package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BedManager {


    private final List<Player> IN_BED;

    public BedManager() {
        this.IN_BED = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if(!IN_BED.contains(player)) {
            IN_BED.add(player);
            check();
        }
    }

    public void removePlayer(Player player) {
        if(IN_BED.contains(player)) {
            IN_BED.remove(player);
            check();
        }
    }

    private int getOnlinePlayers() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }

    public void check() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalEngine.getInstance(), ()->{
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.resetTitle();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Spieler in Bett: §c" + IN_BED.size() + "§f/§c" + getOnlinePlayers() / 2));
            }
            if(IN_BED.size() >= getOnlinePlayers() / 2) {
                Bukkit.broadcastMessage(SurvivalEngine.getInstance().getPREFIX() + "Es wird Tag, da die Hälfte schlief.");
                Bukkit.getServer().getWorlds().forEach((world -> {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setTime(0);
                }));
            }
        },2L);
    }
}
