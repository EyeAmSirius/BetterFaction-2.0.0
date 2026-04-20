package com.betterfactions.modules.claims;

import com.betterfactions.core.service.FactionService;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class ClaimProtectionListener implements Listener {

    private final ClaimService claimService;
    private final FactionService factionService;

    public ClaimProtectionListener(ClaimService claimService, FactionService factionService) {
        this.claimService = claimService;
        this.factionService = factionService;
    }

    private boolean canBuild(Player player, Chunk chunk) {
        UUID uuid = player.getUniqueId();

        return claimService.getFactionByChunk(chunk).map(ownerName ->

                factionService.getFactionByPlayer(uuid)
                        .map(f -> f.getName().equalsIgnoreCase(ownerName))
                        .orElse(false)

        ).orElse(true); // si non claim → autorisé
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!canBuild(e.getPlayer(), e.getBlock().getChunk())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cTu ne peux pas construire ici.");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!canBuild(e.getPlayer(), e.getBlock().getChunk())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cTu ne peux pas casser ici.");
        }
    }
}
