package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.object.Teleporter;
import me.einjojo.survivalengine.recipe.TeleporterRecipe;
import me.einjojo.survivalengine.util.config.TeleporterConfig;
import net.minecraft.sounds.SoundEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

public class TeleportManager {

    private final SurvivalEngine plugin;
    private final TeleporterConfig config;
    private final Map<String, Teleporter> TELEPORTER_MAP;
    private final List<Player> INTERACT_BLACKLIST;
    private final List<Teleporter> SHARD_COOLDOWN;
    private final List<Player> TELEPORTING_PLAYERS;


    public TeleportManager(SurvivalEngine plugin) {
        this.plugin = plugin;
        this.config = new TeleporterConfig(plugin);
        this.TELEPORTER_MAP = new HashMap<>();
        this.INTERACT_BLACKLIST = new ArrayList<>();
        this.TELEPORTING_PLAYERS = new ArrayList<>();
        this.SHARD_COOLDOWN = new ArrayList<>();
    }


    public void createTeleporter(Teleporter teleporter) throws Exception {
        if(!this.TELEPORTER_MAP.containsKey(teleporter.getName())){
            this.TELEPORTER_MAP.put(teleporter.getName(), teleporter);
            plugin.getLogger().log(Level.INFO, "Loaded Teleporter: " + teleporter.getName());
        } else {
            throw new Exception("Der Teleporter \""+teleporter.getName()+"\" existiert bereits");
        }
    }

    public List<Player> getTELEPORTING_PLAYERS() {
        return TELEPORTING_PLAYERS;
    }

    public List<Teleporter> getTeleporterByPlayer(UUID playerUUID) {
        List<Teleporter> teleporterList = new ArrayList<>();
        for(Map.Entry<String, Teleporter> entry : TELEPORTER_MAP.entrySet()){
            if((entry.getValue().getType().equals(Teleporter.Type.PLAYER)) && (entry.getValue().getOwner().equals(playerUUID))) {
                teleporterList.add(entry.getValue());
            }
        }
        return teleporterList;
    }

    public List<Teleporter> getTeleporterByTeam(UUID teamID) {
        List<Teleporter> teleporterList = new ArrayList<>();
        for(Map.Entry<String, Teleporter> entry : TELEPORTER_MAP.entrySet()){
            if((entry.getValue().getType().equals(Teleporter.Type.TEAM)) && (entry.getValue().getOwner().equals(teamID))) {
                teleporterList.add(entry.getValue());
            }
        }
        return teleporterList;
    }

    public void setShardCoolDown(Teleporter teleporter) {
        if(!SHARD_COOLDOWN.contains(teleporter)) {
            SHARD_COOLDOWN.add(teleporter);

            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,  () -> {
                SHARD_COOLDOWN.remove(teleporter);
            }, 20*60*10);
        }
    }

    public void teleport(Player player, Teleporter target) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 255));
        getTELEPORTING_PLAYERS().add(player);
        player.teleport(target.getLocation());
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.1f, 3);
        target.setUsedCounter(target.getUsedCounter() + 1);
        getTELEPORTING_PLAYERS().remove(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }, 10L);
    }

    public boolean hasShardCoolDown(Teleporter teleporter) {
        return SHARD_COOLDOWN.contains(teleporter);
    }

    public void malfunctionTeleporter(Player player, Entity teleporter) {
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
        player.sendMessage(plugin.getPREFIX() + "Fehlerhafter Teleporter wurde entfernt");
        player.getInventory().addItem(TeleporterRecipe.getItemStack());
        teleporter.remove();
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

    public void deleteTeleporterAndEntity(Teleporter teleporter, Player player) {
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
            List<String> linkedTeleporter = configurationSection.getStringList(teleporter + ".linked");

            Teleporter.Type type;
            if(configurationSection.getString(teleporter + ".type").contains("team")) {
                type = Teleporter.Type.TEAM;
            } else {
                type = Teleporter.Type.PLAYER;
            }


            String[] args = locationString.split(" ");
            Location location = new Location(Bukkit.getWorld(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            try {
                createTeleporter(new Teleporter(
                        teleporter,
                        location,
                        usedCounter,
                        activated,
                        owner,
                        linkedTeleporter,
                        type
                ));
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Could not load teleporter: " + e.getMessage());

            }
        });



    }
}
