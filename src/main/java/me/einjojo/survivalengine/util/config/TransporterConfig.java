package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.object.SurvivalPlayer;

import java.util.UUID;

public class TransporterConfig extends ConfigFile {

    public TransporterConfig(SurvivalEngine plugin) {
        super(plugin, "mobs.yml");
    }

    public void saveChicken(UUID uuid, TransportChicken chicken) {
        getFile().set("chicken." + uuid.toString(), chicken.serialize());
        saveFile();
    }
}
