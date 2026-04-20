package com.betterfactions.commands;

import com.betterfactions.core.domain.FactionRelation;
import com.betterfactions.core.service.FactionService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionRelationCommand implements CommandExecutor {

    private final FactionService factionService;

    public FactionRelationCommand(FactionService factionService) {
        this.factionService = factionService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§eUsage : /f <ally|enemy|truce|neutral> <faction>");
            return true;
        }

        String sub = args[0].toLowerCase();
        String targetName = args[1];

        return factionService.getFactionByPlayer(player.getUniqueId()).map(faction -> {

            if (faction.getName().equalsIgnoreCase(targetName)) {
                player.sendMessage("§cTu ne peux pas définir une relation avec ta propre faction.");
                return true;
            }

            FactionRelation rel = switch (sub) {
                case "ally" -> FactionRelation.ALLY;
                case "enemy" -> FactionRelation.ENEMY;
                case "truce" -> FactionRelation.TRUCE;
                case "neutral" -> FactionRelation.NEUTRAL;
                default -> null;
            };

            if (rel == null) {
                player.sendMessage("§cRelation inconnue.");
                return true;
            }

            boolean ok = factionService.setRelation(faction.getName(), targetName, rel);
            if (!ok) {
                player.sendMessage("§cFaction cible introuvable.");
                return true;
            }

            player.sendMessage("§aRelation mise à jour : §e" + targetName + " §7→ §b" + rel);
            return true;

        }).orElseGet(() -> {
            player.sendMessage("§cTu n'as pas de faction.");
            return true;
        });
    }
}