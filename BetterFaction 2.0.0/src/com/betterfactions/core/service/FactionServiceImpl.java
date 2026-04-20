package com.betterfactions.core.service;

import com.betterfactions.core.domain.*;
import com.betterfactions.core.repository.FactionRelationsRepository;
import com.betterfactions.core.repository.FactionRepository;

import java.util.*;

public class FactionServiceImpl implements FactionService {

    private final FactionRepository repository;
    private final FactionRelationsRepository relationsRepo;

    // joueur -> nom de faction
    private final Map<UUID, String> playerFactions = new HashMap<>();

    // invitations : nomFaction -> joueurs invités
    private final Map<String, Set<UUID>> invitations = new HashMap<>();

    public FactionServiceImpl(FactionRepository repository,
                              FactionRelationsRepository relationsRepo) {
        this.repository = repository;
        this.relationsRepo = relationsRepo;

        // 🔥 IMPORTANT : recharger les membres depuis YAML
        loadPlayerFactions();
    }

    // ----------------------------------------------------
    // RECHARGEMENT DES MEMBRES AU DÉMARRAGE
    // ----------------------------------------------------
    private void loadPlayerFactions() {
        for (Faction faction : repository.getAllFactions()) {
            for (UUID uuid : faction.getMembers().keySet()) {
                playerFactions.put(uuid, faction.getName());
            }
        }
    }

    @Override
    public Optional<Faction> getFactionByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public Optional<Faction> getFactionByPlayer(UUID uuid) {
        String name = playerFactions.get(uuid);
        if (name == null) return Optional.empty();
        return repository.getByName(name);
    }

    @Override
    public Faction createFaction(UUID leader, String name) {
        Faction faction = new Faction(name, leader);
        repository.save(faction);

        // 🔥 enregistrer le leader
        playerFactions.put(leader, name);

        return faction;
    }

    @Override
    public boolean deleteFaction(String name) {
        Optional<Faction> opt = repository.getByName(name);
        if (opt.isEmpty()) return false;

        Faction faction = opt.get();

        // retirer tous les joueurs de la map
        faction.getMembers().keySet().forEach(playerFactions::remove);

        repository.delete(name);
        return true;
    }

    @Override
    public boolean invitePlayer(String factionName, UUID target) {
        if (repository.getByName(factionName).isEmpty()) return false;
        invitations.computeIfAbsent(factionName, k -> new HashSet<>()).add(target);
        return true;
    }

    @Override
    public boolean joinFaction(UUID player, String factionName) {
        Optional<Faction> opt = repository.getByName(factionName);
        if (opt.isEmpty()) return false;

        Set<UUID> invited = invitations.getOrDefault(factionName, Collections.emptySet());
        if (!invited.contains(player)) return false;

        // quitter l’ancienne faction si besoin
        getFactionByPlayer(player).ifPresent(old -> {
            old.removeMember(player);
            repository.save(old);
        });

        Faction faction = opt.get();

        // 🔥 ajouter le joueur dans la faction
        faction.addMember(player, FactionRole.RECRUIT);
        repository.save(faction);

        // 🔥 enregistrer dans playerFactions
        playerFactions.put(player, factionName);

        invited.remove(player);

        return true;
    }

    @Override
    public boolean leaveFaction(UUID player) {
        Optional<Faction> opt = getFactionByPlayer(player);
        if (opt.isEmpty()) return false;

        Faction faction = opt.get();

        faction.removeMember(player);
        repository.save(faction);

        playerFactions.remove(player);

        return true;
    }

    @Override
    public boolean setRelation(String f1, String f2, FactionRelation relation) {
        if (repository.getByName(f1).isEmpty()) return false;
        if (repository.getByName(f2).isEmpty()) return false;

        relationsRepo.setRelation(f1, f2, relation);
        return true;
    }

    @Override
    public FactionRelation getRelation(String f1, String f2) {
        if (f1.equalsIgnoreCase(f2)) return FactionRelation.NEUTRAL;
        return relationsRepo.getRelation(f1, f2);
    }

    @Override
    public Set<String> getAllies(String faction) {
        return getByRelation(faction, FactionRelation.ALLY);
    }

    @Override
    public Set<String> getEnemies(String faction) {
        return getByRelation(faction, FactionRelation.ENEMY);
    }

    @Override
    public Set<String> getTruces(String faction) {
        return getByRelation(faction, FactionRelation.TRUCE);
    }

    private Set<String> getByRelation(String faction, FactionRelation rel) {
        Set<String> result = new HashSet<>();
        Map<String, Map<String, FactionRelation>> all = relationsRepo.getAllRelations();
        all.getOrDefault(faction, Map.of()).forEach((other, r) -> {
            if (r == rel) result.add(other);
        });
        return result;
    }
}