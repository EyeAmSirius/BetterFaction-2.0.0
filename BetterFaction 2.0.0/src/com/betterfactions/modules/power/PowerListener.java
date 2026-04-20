package com.betterfactions.modules.power;

import com.betterfactions.core.service.FactionService;
import org.bukkit.event.Listener;

public class PowerListener implements Listener {

    private final FactionService factionService;

    public PowerListener(FactionService factionService) {
        this.factionService = factionService;
    }

    // À compléter si tu veux un vrai système de power
}