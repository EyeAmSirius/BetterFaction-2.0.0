package com.betterfactions.modules.raid;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RaidProtectionListener implements Listener {

    private final RaidService raidService;

    public RaidProtectionListener(RaidService raidService) {
        this.raidService = raidService;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        // À compléter si tu veux lier ça aux claims + factions
        // pour l’instant, rien de bloquant
    }
}
