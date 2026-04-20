package com.betterfactions.core.repository;

import com.betterfactions.core.domain.FactionRelation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlFactionRelationsRepository implements FactionRelationsRepository {

    private final File file;
    private final FileConfiguration config;

    public YamlFactionRelationsRepository(File dataFolder) {
        this.file = new File(dataFolder, "faction_relations.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void setRelation(String f1, String f2, FactionRelation relation) {
        config.set("relations." + f1 + "." + f2, relation.name());
        config.set("relations." + f2 + "." + f1, relation.name());
        save();
    }

    @Override
    public FactionRelation getRelation(String f1, String f2) {
        String path = "relations." + f1 + "." + f2;
        String val = config.getString(path);
        if (val == null) return FactionRelation.NEUTRAL;
        return FactionRelation.valueOf(val);
    }

    @Override
    public Map<String, Map<String, FactionRelation>> getAllRelations() {
        Map<String, Map<String, FactionRelation>> result = new HashMap<>();
        if (!config.contains("relations")) return result;

        for (String f1 : config.getConfigurationSection("relations").getKeys(false)) {
            Map<String, FactionRelation> inner = new HashMap<>();
            for (String f2 : config.getConfigurationSection("relations." + f1).getKeys(false)) {
                String val = config.getString("relations." + f1 + "." + f2);
                inner.put(f2, FactionRelation.valueOf(val));
            }
            result.put(f1, inner);
        }
        return result;
    }

    @Override
    public void setAllRelations(Map<String, Map<String, FactionRelation>> data) {
        config.set("relations", null);
        data.forEach((f1, map) -> map.forEach((f2, rel) ->
                config.set("relations." + f1 + "." + f2, rel.name())
        ));
        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}