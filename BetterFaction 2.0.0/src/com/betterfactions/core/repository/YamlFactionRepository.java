package com.betterfactions.core.repository;

import com.betterfactions.core.domain.Faction;
import com.betterfactions.core.domain.FactionMember;
import com.betterfactions.core.domain.FactionRole;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlFactionRepository implements FactionRepository {

    private final File file;
    private final FileConfiguration config;

    private final Map<String, Faction> factions = new HashMap<>();

    public YamlFactionRepository(File dataFolder) {

        if (!dataFolder.exists()) dataFolder.mkdirs();

        this.file = new File(dataFolder, "factions.yml");

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(file);

        loadAll();
    }

    @Override
    public void save(Faction faction) {
        factions.put(faction.getName(), faction);
        saveAll();
    }

    @Override
    public Optional<Faction> getByName(String name) {
        return Optional.ofNullable(factions.get(name));
    }

    @Override
    public void delete(String name) {
        factions.remove(name);
        saveAll();
    }

    // 🔥 MÉTHODE MANQUANTE — OBLIGATOIRE
    @Override
    public List<Faction> getAllFactions() {
        return new ArrayList<>(factions.values());
    }

    // -------------------------
    // LOADING
    // -------------------------
    private void loadAll() {

        factions.clear();

        if (!config.contains("factions")) return;

        for (String name : config.getConfigurationSection("factions").getKeys(false)) {

            String base = "factions." + name;

            UUID leader = UUID.fromString(config.getString(base + ".leader"));
            Faction faction = new Faction(name, leader);

            // members
            if (config.contains(base + ".members")) {
                for (String uuidStr : config.getConfigurationSection(base + ".members").getKeys(false)) {
                    UUID uuid = UUID.fromString(uuidStr);
                    String roleStr = config.getString(base + ".members." + uuidStr);
                    FactionRole role = FactionRole.valueOf(roleStr);
                    faction.addMember(uuid, role);
                }
            }

            factions.put(name, faction);
        }
    }

    // -------------------------
    // SAVING
    // -------------------------
    private void saveAll() {

        config.set("factions", null);

        for (Faction faction : factions.values()) {

            String base = "factions." + faction.getName();

            faction.getLeader().ifPresent(leader ->
                    config.set(base + ".leader", leader.toString())
            );

            for (Map.Entry<UUID, FactionMember> entry : faction.getMembers().entrySet()) {
                config.set(base + ".members." + entry.getKey(), entry.getValue().getRole().name());
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}