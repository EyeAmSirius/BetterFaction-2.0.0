package com.betterfactions.modules.claims;

import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClaimServiceImpl implements ClaimService {

    private final Map<String, String> claims = new HashMap<>();
    // key = world:x:z, value = factionName

    private String key(Chunk chunk) {
        return chunk.getWorld().getName() + ":" + chunk.getX() + ":" + chunk.getZ();
    }

    @Override
    public boolean claimChunk(String factionName, Chunk chunk) {
        String k = key(chunk);
        if (claims.containsKey(k)) return false;
        claims.put(k, factionName);
        return true;
    }

    @Override
    public boolean unclaimChunk(Chunk chunk) {
        String k = key(chunk);
        if (!claims.containsKey(k)) return false;
        claims.remove(k);
        return true;
    }

    @Override
    public boolean isClaimed(Chunk chunk) {
        return claims.containsKey(key(chunk));
    }

    @Override
    public Optional<String> getFactionByChunk(Chunk chunk) {
        return Optional.ofNullable(claims.get(key(chunk)));
    }
}