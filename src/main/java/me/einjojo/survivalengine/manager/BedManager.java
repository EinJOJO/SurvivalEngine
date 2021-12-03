package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BedManager {


    private final List<Player> IN_BED;

    public BedManager() {
        this.IN_BED = new ArrayList<>();
    }

    private void addPlayer(Player player) {
        if(IN_BED.contains(player)) {
            IN_BED.add(player);
        }
    }

    private void removePlayer(Player player) {
        IN_BED.remove(player);
    }

    private int getOnlinePlayers() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }
}
