package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.SurvivalPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerConfig extends ConfigFile{

    protected PlayerConfig(SurvivalEngine plugin) {
        super(plugin, "player.yml");
    }

    public void savePlayer(SurvivalPlayer survivalPlayer) {
        UUID pUUID = survivalPlayer.getPlayer().getUniqueId();

        getFile().set("settings." + pUUID.toString(), survivalPlayer.serialize());
        saveFile();
    }
}
