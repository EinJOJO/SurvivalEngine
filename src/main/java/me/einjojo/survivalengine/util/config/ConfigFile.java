package me.einjojo.survivalengine.util.config;

import me.einjojo.survivalengine.SurvivalEngine;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public abstract class ConfigFile {

    private final SurvivalEngine plugin;
    private final File file;
    private final FileConfiguration configuration;

    protected ConfigFile(SurvivalEngine plugin, String fileName)  {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        loadFile();
    }

    public void loadFile() {
        if (!file.exists()) {
            configuration.options().copyDefaults(true);
            saveFile();
        } else {
            try {
                configuration.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                plugin.getLogger().log(Level.SEVERE, null, e);
            }
        }
    }

    public void saveFile() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, null, e);
        }
    }

    public FileConfiguration getFile() {
        return configuration;
    }
}
