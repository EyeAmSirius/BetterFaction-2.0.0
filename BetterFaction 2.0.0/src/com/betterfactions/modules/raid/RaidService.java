package com.betterfactions.modules.raid;

import java.util.Optional;

public interface RaidService {

    boolean startRaid(String attacker, String defender);

    boolean isUnderRaid(String faction);

    Optional<String> getAttacker(String defender);

    boolean hasShield(String faction);

    void applyShield(String faction, long durationMillis);
}
