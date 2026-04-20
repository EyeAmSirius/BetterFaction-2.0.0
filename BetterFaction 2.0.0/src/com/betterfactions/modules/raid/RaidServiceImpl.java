package com.betterfactions.modules.raid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RaidServiceImpl implements RaidService {

    private final Map<String, String> activeRaids = new HashMap<>();
    private final Map<String, Long> shields = new HashMap<>();

    @Override
    public boolean startRaid(String attacker, String defender) {

        if (hasShield(defender)) return false;
        if (activeRaids.containsKey(defender)) return false;

        activeRaids.put(defender, attacker);
        return true;
    }

    @Override
    public boolean isUnderRaid(String faction) {
        return activeRaids.containsKey(faction);
    }

    @Override
    public Optional<String> getAttacker(String defender) {
        return Optional.ofNullable(activeRaids.get(defender));
    }

    @Override
    public boolean hasShield(String faction) {
        return shields.getOrDefault(faction, 0L) > System.currentTimeMillis();
    }

    @Override
    public void applyShield(String faction, long durationMillis) {
        shields.put(faction, System.currentTimeMillis() + durationMillis);
    }
}