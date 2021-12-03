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

    public void addPlayer(Player player) {
        if(!IN_BED.contains(player)) {
            IN_BED.add(player);
        }
        check();
    }

    public void removePlayer(Player player) {
        IN_BED.remove(player);
        check();
    }

    private int getOnlinePlayers() {
        return Bukkit.getServer().getOnlinePlayers().size();
    }

    public void check() {
        if(IN_BED.size() >= getOnlinePlayers() / 2) {
            Bukkit.getServer().getWorlds().forEach((world -> {
                world.setThundering(false);
                world.setStorm(false);
                world.setTime(0);
            }));
        }
    }
}
