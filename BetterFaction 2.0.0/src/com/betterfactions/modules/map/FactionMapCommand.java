package com.betterfactions.modules.map;

import com.betterfactions.modules.claims.ClaimService;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class FactionMapCommand {

    private final ClaimService claimService;

    public FactionMapCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    /**
     * Génère une mini‑map ASCII autour du joueur.
     * Cette méthode NE gère PAS les commandes Bukkit.
     * Elle renvoie simplement une String prête à être envoyée.
     */
    public String generateMap(Player player, int radius) {

        StringBuilder map = new StringBuilder();
        Chunk center = player.getLocation().getChunk();

        map.append("§7=== §6FMap §7===\n");

        for (int dz = -radius; dz <= radius; dz++) {

            for (int dx = -radius; dx <= radius; dx++) {

                Chunk c = center.getWorld().getChunkAt(center.getX() + dx, center.getZ() + dz);

                // Le joueur au centre
                if (dx == 0 && dz == 0) {
                    map.append("§e◆");
                    continue;
                }

                // Claim
                if (claimService.isClaimed(c)) {
                    map.append("§a■");
                } else {
                    map.append("§7□");
                }
            }

            map.append("\n");
        }

        return map.toString();
    }
}