package com.betterfactions.commands;

import com.betterfactions.core.service.FactionService;
import com.betterfactions.modules.raid.RaidService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionRaidCommand implements CommandExecutor {

    private final FactionService factionService;
    private final RaidService raidService;

    public FactionRaidCommand(FactionService factionService, RaidService raidService) {
        this.factionService = factionService;
        this.raidService = raidService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§eUsage : /fraid <faction>");
            return true;
        }

        String target = args[0];

        return factionService.getFactionByPlayer(player.getUniqueId()).map(attackerFaction -> {

            if (attackerFaction.getName().equalsIgnoreCase(target)) {
                player.sendMessage("§cTu ne peux pas raid ta propre faction.");
                return true;
            }

            boolean ok = raidService.startRaid(attackerFaction.getName(), target);

            if (!ok) {
                player.sendMessage("§cImpossible de lancer le raid (shield actif ou raid déjà en cours).");
                return true;
            }

            player.sendMessage("§aRaid lancé contre §e" + target + " !");
            return true;

        }).orElseGet(() -> {
            player.sendMessage("§cTu n'as pas de faction.");
            return true;
        });
    }
}
