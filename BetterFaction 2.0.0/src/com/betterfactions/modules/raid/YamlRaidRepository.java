package com.betterfactions.modules.raid;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class YamlRaidRepository implements RaidRepository {

    private final File file;
    private final FileConfiguration config;

    public YamlRaidRepository(File dataFolder) {
        this.file = new File(dataFolder, "raids.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void setActiveRaid(String defender, String attacker) {
        config.set("active." + defender, attacker);
        save();
    }

    @Override
    public void clearRaid(String defender) {
        config.set("active." + defender, null);
        save();
    }

    @Override
    public Optional<String> getAttacker(String defender) {
        return Optional.ofNullable(config.getString("active." + defender));
    }

    @Override
    public void setShield(String faction, long until) {
        config.set("shield." + faction, until);
        save();
    }

    @Override
    public Optional<Long> getShield(String faction) {
        if (!config.contains("shield." + faction)) return Optional.empty();
        return Optional.of(config.getLong("shield." + faction));
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}