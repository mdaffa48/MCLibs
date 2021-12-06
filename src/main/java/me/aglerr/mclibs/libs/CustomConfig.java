package me.aglerr.mclibs.libs;

import me.aglerr.mclibs.MCLibs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final File file;
    private FileConfiguration config;

    public CustomConfig(String configName, String directory) {
        Plugin plugin = MCLibs.INSTANCE;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (directory == null) {
            file = new File(plugin.getDataFolder(), configName);

            if (!file.exists()) {
                plugin.saveResource(configName, false);
            }

        } else {
            File directoryFile = new File(plugin.getDataFolder() + File.separator + directory);
            if (!directoryFile.exists()) {
                directoryFile.mkdirs();
            }

            file = new File(plugin.getDataFolder() + File.separator + directory, configName);

            if (!file.exists()) {
                plugin.saveResource(directory + File.separator + configName, false);
            }

        }

        config = YamlConfiguration.loadConfiguration(file);

    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}
