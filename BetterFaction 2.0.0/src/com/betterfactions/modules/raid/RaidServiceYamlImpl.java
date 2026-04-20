package com.betterfactions.modules.raid;

import java.util.Optional;

public class RaidServiceYamlImpl implements RaidService {

    private final RaidRepository repo;

    public RaidServiceYamlImpl(RaidRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean startRaid(String attacker, String defender) {
        if (hasShield(defender)) return false;
        if (isUnderRaid(defender)) return false;
        repo.setActiveRaid(defender, attacker);
        return true;
    }

    @Override
    public boolean isUnderRaid(String faction) {
        return repo.getAttacker(faction).isPresent();
    }

    @Override
    public Optional<String> getAttacker(String defender) {
        return repo.getAttacker(defender);
    }

    @Override
    public boolean hasShield(String faction) {
        return repo.getShield(faction)
                .map(until -> until > System.currentTimeMillis())
                .orElse(false);
    }

    @Override
    public void applyShield(String faction, long durationMillis) {
        repo.setShield(faction, System.currentTimeMillis() + durationMillis);
    }
}