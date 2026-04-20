package com.betterfactions.core.domain;

import java.util.*;

public class Faction {

    private final String name;
    private final Map<UUID, FactionMember> members = new HashMap<>();

    public Faction(String name, UUID leader) {
        this.name = name;
        members.put(leader, new FactionMember(leader, FactionRole.LEADER));
    }

    public String getName() {
        return name;
    }

    public Map<UUID, FactionMember> getMembers() {
        return members;
    }

    public boolean isMember(UUID uuid) {
        return members.containsKey(uuid);
    }

    public Optional<FactionMember> getMember(UUID uuid) {
        return Optional.ofNullable(members.get(uuid));
    }

    public void addMember(UUID uuid, FactionRole role) {
        members.put(uuid, new FactionMember(uuid, role));
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public Optional<UUID> getLeader() {
        return members.entrySet().stream()
                .filter(e -> e.getValue().getRole() == FactionRole.LEADER)
                .map(Map.Entry::getKey)
                .findFirst();
    }
}