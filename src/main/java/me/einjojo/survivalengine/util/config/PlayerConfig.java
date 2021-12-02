package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerConfig extends ConfigFile{

    public PlayerConfig(SurvivalEngine plugin) {
        super(plugin, "player.yml");
    }

    public void savePlayer(UUID uuid, SurvivalPlayer survivalPlayer) {
        getFile().set("player." + uuid.toString(), survivalPlayer.serialize());
        saveFile();
    }
}
