package com.betterfactions.modules.claims;

import org.bukkit.Chunk;

import java.util.Optional;

public interface ClaimService {

    boolean claimChunk(String factionName, Chunk chunk);

    boolean unclaimChunk(Chunk chunk);

    boolean isClaimed(Chunk chunk);

    Optional<String> getFactionByChunk(Chunk chunk);
}
