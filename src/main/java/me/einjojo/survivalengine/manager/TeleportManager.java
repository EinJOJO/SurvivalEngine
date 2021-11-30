package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.util.config.TeleporterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class TeleportManager {

    private final SurvivalEngine plugin;
    private final TeleporterConfig config;
    private final Map<String, Teleporter> teleporterMap;


    public TeleportManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new TeleporterConfig(plugin);
        this.teleporterMap = new HashMap<>();
    }


    public boolean createTeleporter(Teleporter teleporter) {
        if(!this.teleporterMap.containsKey(teleporter.getName())){
            this.teleporterMap.put(teleporter.getName(), teleporter);
            plugin.getLogger().log(Level.INFO, "Loaded Teleporter: " + teleporter.getName());
            return true;
        }
        return false;
    }

    public Teleporter getTeleporter(String name) {

        return teleporterMap.get(name.substring(2));
    }

    public void deleteTeleporter(Teleporter teleporter) {
        this.teleporterMap.remove(teleporter.getName());
        plugin.getLogger().log(Level.INFO,"Deleted Teleporter: " + teleporter.getName());
    }

    public void save(){
        config.getFile().set("teleporter", null);
        teleporterMap.forEach(config::saveTeleporter);
        config.saveFile();
        plugin.getLogger().log(Level.INFO, "Saved all teleporters");
    }

    public void load() {
        FileConfiguration configuration = config.getFile();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("teleporter");
        if(configurationSection == null) return;

        Set<String> teleporterSet = configurationSection.getKeys(false);
        teleporterSet.forEach(teleporter -> {
            String locationString = configuration.getString("teleporter." + teleporter + ".location");
            UUID owner = UUID.fromString(configuration.getString("teleporter." + teleporter + ".owner"));
            boolean activated = configuration.isBoolean("teleporter." + teleporter + ".activated") && configuration.getBoolean("teleporter." + teleporter + ".activated");
            int usedCounter = configuration.getInt("teleporter." + teleporter + ".location");

            String[] args = locationString.split(" ");
            Location location = new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            createTeleporter(new Teleporter(
                    teleporter,
                    location,
                    usedCounter,
                    activated,
                    owner
            ));
        });



    }
}
