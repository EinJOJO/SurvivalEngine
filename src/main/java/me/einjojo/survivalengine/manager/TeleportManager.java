package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.util.config.TeleporterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class TeleportManager {

    private final SurvivalEngine plugin;
    private final TeleporterConfig config;
    private final Map<String, Teleporter> TELEPORTER_MAP;
    private final List<Player> INTERACT_BLACKLIST;
    private final List<Player> TELEPORTING_PLAYERS;


    public TeleportManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new TeleporterConfig(plugin);
        this.TELEPORTER_MAP = new HashMap<>();
        this.INTERACT_BLACKLIST = new ArrayList<>();
        this.TELEPORTING_PLAYERS = new ArrayList<>();
    }


    public boolean createTeleporter(Teleporter teleporter) {
        if(!this.TELEPORTER_MAP.containsKey(teleporter.getName())){
            this.TELEPORTER_MAP.put(teleporter.getName(), teleporter);
            plugin.getLogger().log(Level.INFO, "Loaded Teleporter: " + teleporter.getName());
            return true;
        }
        return false;
    }

    public List<Player> getTELEPORTING_PLAYERS() {
        return TELEPORTING_PLAYERS;
    }

    public List<Teleporter> getTeleporterByPlayer(UUID playerUUID) {
        List<Teleporter> teleporterList = new ArrayList<>();
        for(Map.Entry<String, Teleporter> entry : TELEPORTER_MAP.entrySet()){
            if(entry.getValue().getOwner().equals(playerUUID)) {
                teleporterList.add(entry.getValue());
            }
        }
        return teleporterList;
    }

    public Teleporter getTeleporter(String name) {
        return TELEPORTER_MAP.get(name.substring(2));
    }

    public List<Player> getINTERACT_BLACKLIST() {
        return INTERACT_BLACKLIST;
    }

    public void deleteTeleporter(Teleporter teleporter) {
        this.TELEPORTER_MAP.remove(teleporter.getName());
        plugin.getLogger().log(Level.INFO,"Deleted Teleporter: " + teleporter.getName());
    }

    public void deleteTeleporter(Teleporter teleporter, Player player) {
        List<Entity> entities = player.getNearbyEntities(7,7,7);
        for (Entity entity : entities) {
            if((entity.getType() == EntityType.ENDER_CRYSTAL) && (entity.getCustomName().equals("§c" + teleporter.getName()))) {
                entity.remove();
            }
        }

        deleteTeleporter(teleporter);
    }

    public void save(){
        config.getFile().set("teleporter", null);
        TELEPORTER_MAP.forEach(config::saveTeleporter);
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
            int usedCounter = configuration.getInt("teleporter." + teleporter + ".used");

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
