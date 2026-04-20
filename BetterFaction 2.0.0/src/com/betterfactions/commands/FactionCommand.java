package com.betterfactions.commands;

import com.betterfactions.core.domain.Faction;
import com.betterfactions.core.service.FactionService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionCommand implements CommandExecutor {

    private final FactionService factionService;

    public FactionCommand(FactionService factionService) {
        this.factionService = factionService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            player.sendMessage("§e/f create <nom>");
            player.sendMessage("§e/f invite <joueur>");
            player.sendMessage("§e/f join <faction>");
            player.sendMessage("§e/f leave");
            player.sendMessage("§e/f ally|enemy|truce|neutral <faction>");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage : /f create <nom>");
                    return true;
                }
                String name = args[1];
                if (factionService.getFactionByName(name).isPresent()) {
                    player.sendMessage("§cCette faction existe déjà.");
                    return true;
                }
                factionService.createFaction(uuid, name);
                player.sendMessage("§aFaction créée : §e" + name);
            }

            case "invite" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage : /f invite <joueur>");
                    return true;
                }
                factionService.getFactionByPlayer(uuid).ifPresentOrElse(faction -> {
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage("§cJoueur introuvable.");
                        return;
                    }
                    factionService.invitePlayer(faction.getName(), target.getUniqueId());
                    player.sendMessage("§aInvitation envoyée à §e" + target.getName());
                }, () -> player.sendMessage("§cTu n'as pas de faction."));
            }

            case "join" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage : /f join <faction>");
                    return true;
                }
                boolean ok = factionService.joinFaction(uuid, args[1]);
                if (ok) player.sendMessage("§aTu as rejoint §e" + args[1]);
                else player.sendMessage("§cImpossible de rejoindre cette faction.");
            }

            case "leave" -> {
                boolean ok = factionService.leaveFaction(uuid);
                if (ok) player.sendMessage("§aTu as quitté ta faction.");
                else player.sendMessage("§cTu n'as pas de faction.");
            }

            default -> player.sendMessage("§cSous-commande inconnue.");
        }

        return true;
    }
}
