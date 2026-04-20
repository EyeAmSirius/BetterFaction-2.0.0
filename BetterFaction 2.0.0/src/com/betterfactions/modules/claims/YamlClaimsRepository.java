package com.betterfactions.modules.claims;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class YamlClaimsRepository implements ClaimsRepository {

    private final File file;
    private final FileConfiguration config;

    public YamlClaimsRepository(File dataFolder) {
        this.file = new File(dataFolder, "claims.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    private String path(String world, int x, int z) {
        return "claims." + world + "." + x + "." + z;
    }

    @Override
    public void setClaim(String world, int x, int z, String faction) {
        config.set(path(world, x, z), faction);
        save();
    }

    @Override
    public void removeClaim(String world, int x, int z) {
        config.set(path(world, x, z), null);
        save();
    }

    @Override
    public Optional<String> getFaction(String world, int x, int z) {
        String val = config.getString(path(world, x, z));
        return Optional.ofNullable(val);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}