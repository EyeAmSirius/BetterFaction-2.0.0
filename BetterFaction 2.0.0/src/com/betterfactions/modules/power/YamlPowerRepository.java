package com.betterfactions.modules.power;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class YamlPowerRepository implements PowerRepository {

    private final File file;
    private final FileConfiguration config;

    public YamlPowerRepository(File dataFolder) {
        this.file = new File(dataFolder, "power.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void setPower(String faction, double power) {
        config.set("power." + faction, power);
        save();
    }

    @Override
    public Optional<Double> getPower(String faction) {
        if (!config.contains("power." + faction)) return Optional.empty();
        return Optional.of(config.getDouble("power." + faction));
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}