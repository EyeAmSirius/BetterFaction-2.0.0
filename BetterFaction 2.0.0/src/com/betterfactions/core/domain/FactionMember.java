package com.betterfactions.core.domain;

import java.util.UUID;

public class FactionMember {

    private final UUID uuid;
    private FactionRole role;

    public FactionMember(UUID uuid, FactionRole role) {
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public FactionRole getRole() {
        return role;
    }

    public void setRole(FactionRole role) {
        this.role = role;
    }
}