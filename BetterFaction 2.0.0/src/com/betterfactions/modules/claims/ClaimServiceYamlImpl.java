package com.betterfactions.modules.claims;

import org.bukkit.Chunk;

import java.util.Optional;

public class ClaimServiceYamlImpl implements ClaimService {

    private final ClaimsRepository repo;

    public ClaimServiceYamlImpl(ClaimsRepository repo) {
        this.repo = repo;
    }

    private String world(Chunk c) { return c.getWorld().getName(); }
    private int x(Chunk c) { return c.getX(); }
    private int z(Chunk c) { return c.getZ(); }

    @Override
    public boolean claimChunk(String factionName, Chunk chunk) {
        if (isClaimed(chunk)) return false;
        repo.setClaim(world(chunk), x(chunk), z(chunk), factionName);
        return true;
    }

    @Override
    public boolean unclaimChunk(Chunk chunk) {
        if (!isClaimed(chunk)) return false;
        repo.removeClaim(world(chunk), x(chunk), z(chunk));
        return true;
    }

    @Override
    public boolean isClaimed(Chunk chunk) {
        return repo.getFaction(world(chunk), x(chunk), z(chunk)).isPresent();
    }

    @Override
    public Optional<String> getFactionByChunk(Chunk chunk) {
        return repo.getFaction(world(chunk), x(chunk), z(chunk));
    }
}