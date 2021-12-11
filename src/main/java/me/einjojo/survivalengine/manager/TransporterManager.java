package me.einjojo.survivalengine.manager;

import me.einjojo.survivalengine.SurvivalEngine;
import me.einjojo.survivalengine.entity.TransportChicken;
import me.einjojo.survivalengine.util.config.TransporterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TransporterManager {

    private final HashMap<UUID, TransportChicken> CHICKEN_MAP;
    private final TransporterConfig config;
    private final SurvivalEngine plugin;

    public TransporterManager(SurvivalEngine plugin) {
        this.CHICKEN_MAP = new HashMap<>();
        this.config = new TransporterConfig(plugin);
        this.plugin = plugin;
    }


    public void removeChicken(UUID ownerUUID) {
        TransportChicken transportChicken = CHICKEN_MAP.get(ownerUUID);

        if(transportChicken != null) {
            if (transportChicken.isSpawned()) {
                transportChicken.remove();
            }
            CHICKEN_MAP.remove(ownerUUID);
            plugin.getLogger().info("Removed Chicken: " + ownerUUID.toString());
        }
    }

    public TransportChicken createTransportChicken(Location location, UUID owner, Inventory inventory, boolean spawned) {
        if(!CHICKEN_MAP.containsKey(owner)) {
            TransportChicken transportChicken = new TransportChicken(location, owner, inventory, spawned);
            this.CHICKEN_MAP.put(owner, transportChicken);
            return transportChicken;
        } else {
            return getTransportChicken(owner);
        }
    }

    public TransportChicken createTransportChicken(Location location, UUID owner) {
        return createTransportChicken(location, owner, createInventory(), false);
    }


    public TransportChicken getTransportChicken(UUID uuid) {
        return CHICKEN_MAP.get(uuid);
    }

    public TransportChicken getTransportChicken(Entity entity) {
        String name = entity.getCustomName();
        if(name == null) {
            return null;
        }

        if(!name.contains("'s Transporter")) {
            return null;
        }

        String ownerName = name.substring(2, name.indexOf("'s"));
        Player player = Bukkit.getPlayer(ownerName);
        if(player == null) {
            remove(entity);
            return null;
        }

        return CHICKEN_MAP.get(player.getUniqueId());
    }


    public void remove(Entity entity) {
        entity.remove();
    }



    public void clearMobs() {
        for (Map.Entry<UUID, TransportChicken> entry : CHICKEN_MAP.entrySet()) {
            entry.getValue().remove();
        }
    }


    public void load() {
        FileConfiguration configuration = config.getFile();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("chicken");
        if(configurationSection == null) return;

        Set<String> chickenSet = configurationSection.getKeys(false);

        chickenSet.forEach((chickenOwner)->{

            boolean spawned = configurationSection.getBoolean(chickenOwner + ".spawned");

            ItemStack[] itemArray = createInventory().getContents();
            ConfigurationSection inventorySection = configurationSection.getConfigurationSection(chickenOwner + ".inventory");
            if(inventorySection == null) {
                return;
            }
            for (String key : inventorySection.getKeys(false)) {
                int i = Integer.parseInt(key);
                itemArray[i] = inventorySection.getItemStack(key);
            }
            int i = 0;


            Inventory inventory = createInventory();
            UUID owner = UUID.fromString(chickenOwner);
            Location location = new Location(Bukkit.getWorld("world"), 0,0,0);
            inventory.setContents(itemArray);

            createTransportChicken(location, owner, inventory, spawned);
        });
        plugin.getLogger().info(String.format("Loaded %d chickens", CHICKEN_MAP.size()));

    }


    public void save() {
        if(CHICKEN_MAP.size() == 0) {
            config.getFile().set("chicken", null);
            config.saveFile();
            return;
        }
        CHICKEN_MAP.forEach(config::saveChicken);
    }


    public static Inventory createInventory() {
        return Bukkit.createInventory(null, 9);
    }

}
