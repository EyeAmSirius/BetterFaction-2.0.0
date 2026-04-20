package com.betterfactions.modules.claims;

import java.util.Optional;

public interface ClaimsRepository {

    void setClaim(String world, int x, int z, String faction);

    void removeClaim(String world, int x, int z);

    Optional<String> getFaction(String world, int x, int z);
}
