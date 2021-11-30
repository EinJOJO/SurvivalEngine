package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;

public class TeleporterConfig extends ConfigFile{
    public TeleporterConfig(SurvivalEngine plugin) {
        super(plugin, "teleporter.yml");
    }

    public void saveTeleporter(Teleporter teleporter) {
        getFile().set("teleporter."+teleporter.getName(), teleporter.serialize());
    }

    public void saveTeleporter(String name, Teleporter teleporter) {
        getFile().set("teleporter."+name, teleporter.serialize());
    }
}
