package com.betterfactions.commands;

import com.betterfactions.core.service.FactionService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminFactionCommand implements CommandExecutor {

    private final FactionService factionService;

    public AdminFactionCommand(FactionService factionService) {
        this.factionService = factionService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("betterfactions.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§e/factionadmin delete <faction>");
            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {

            if (args.length < 2) {
                sender.sendMessage("§cUsage: /factionadmin delete <faction>");
                return true;
            }

            String name = args[1];

            if (factionService.deleteFaction(name)) {
                sender.sendMessage("§aFaction supprimée: §f" + name);
            } else {
                sender.sendMessage("§cFaction introuvable: §f" + name);
            }

            return true;
        }

        sender.sendMessage("§cSous-commande inconnue.");
        return true;
    }
}