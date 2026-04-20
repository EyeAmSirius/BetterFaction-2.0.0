package com.betterfactions.core.repository;

import com.betterfactions.core.domain.Faction;

import java.util.List;
import java.util.Optional;

public interface FactionRepository {

    // Récupérer une faction par son nom
    Optional<Faction> getByName(String name);

    // Sauvegarder ou mettre à jour une faction
    void save(Faction faction);

    // Supprimer une faction
    void delete(String name);

    // 🔥 Récupérer toutes les factions (nécessaire pour recharger les membres)
    List<Faction> getAllFactions();
}