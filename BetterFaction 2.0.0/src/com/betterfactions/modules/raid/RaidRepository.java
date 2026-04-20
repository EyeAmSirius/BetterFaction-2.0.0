package com.betterfactions.modules.raid;

import java.util.Optional;

public interface RaidRepository {

    void setActiveRaid(String defender, String attacker);

    void clearRaid(String defender);

    Optional<String> getAttacker(String defender);

    void setShield(String faction, long until);

    Optional<Long> getShield(String faction);
}