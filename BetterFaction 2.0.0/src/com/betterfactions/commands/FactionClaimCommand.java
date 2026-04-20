package com.betterfactions.commands;

import com.betterfactions.core.service.FactionService;
import com.betterfactions.modules.claims.ClaimService;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionClaimCommand implements CommandExecutor {

    private final FactionService factionService;
    private final ClaimService claimService;

    public FactionClaimCommand(FactionService factionService, ClaimService claimService) {
        this.factionService = factionService;
        this.claimService = claimService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e/fclaim claim");
            player.sendMessage("§e/fclaim unclaim");
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();

        switch (args[0].toLowerCase()) {

            case "claim" -> {
                return factionService.getFactionByPlayer(player.getUniqueId()).map(faction -> {

                    boolean ok = claimService.claimChunk(faction.getName(), chunk);
                    if (!ok) {
                        player.sendMessage("§cCe chunk est déjà claim.");
                        return true;
                    }

                    player.sendMessage("§aChunk claim par §e" + faction.getName());
                    return true;

                }).orElseGet(() -> {
                    player.sendMessage("§cTu n'as pas de faction.");
                    return true;
                });
            }

            case "unclaim" -> {
                boolean ok = claimService.unclaimChunk(chunk);
                if (!ok) {
                    player.sendMessage("§cCe chunk n'est pas claim.");
                    return true;
                }
                player.sendMessage("§aChunk unclaim.");
            }

            default -> player.sendMessage("§cUsage : /fclaim <claim|unclaim>");
        }

        return true;
    }
}
