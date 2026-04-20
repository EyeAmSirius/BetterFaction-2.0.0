package com.betterfactions.commands;

import com.betterfactions.core.service.FactionService;
import com.betterfactions.modules.claims.ClaimService;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionMapCommand implements CommandExecutor {

    private final ClaimService claimService;

    public FactionMapCommand(ClaimService claimService, FactionService factionService) {
        this.claimService = claimService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        int radius = 4; // 9x9
        Chunk center = player.getLocation().getChunk();

        player.sendMessage("§7=== §6FMap §7===");

        for (int dz = -radius; dz <= radius; dz++) {
            StringBuilder line = new StringBuilder();
            for (int dx = -radius; dx <= radius; dx++) {
                Chunk c = center.getWorld().getChunkAt(center.getX() + dx, center.getZ() + dz);

                if (dx == 0 && dz == 0) {
                    line.append("§e◆");
                    continue;
                }

                if (claimService.isClaimed(c)) {
                    line.append("§a■");
                } else {
                    line.append("§7□");
                }
            }
            player.sendMessage(line.toString());
        }

        return true;
    }
}
