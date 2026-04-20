package com.betterfactions.core.service;

import com.betterfactions.core.domain.Faction;
import com.betterfactions.core.domain.FactionRelation;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FactionService {

    // --- Récupération ---
    Optional<Faction> getFactionByName(String name);

    Optional<Faction> getFactionByPlayer(UUID uuid);

    // --- Création / Suppression ---
    Faction createFaction(UUID leader, String name);

    boolean deleteFaction(String name);

    // --- Invitations ---
    boolean invitePlayer(String factionName, UUID target);

    boolean joinFaction(UUID player, String factionName);

    boolean leaveFaction(UUID player);

    // --- Relations ---
    boolean setRelation(String f1, String f2, FactionRelation relation);

    FactionRelation getRelation(String f1, String f2);

    Set<String> getAllies(String faction);

    Set<String> getEnemies(String faction);

    Set<String> getTruces(String faction);
}