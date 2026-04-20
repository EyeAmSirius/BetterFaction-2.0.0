package com.betterfactions.commands;

import com.betterfactions.modules.chat.ChatMode;
import com.betterfactions.modules.chat.ChatModeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionChatModeCommand implements CommandExecutor {

    private final ChatModeManager chatModes;

    public FactionChatModeCommand(ChatModeManager chatModes) {
        this.chatModes = chatModes;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        if (label.equalsIgnoreCase("fc")) {
            chatModes.setMode(player.getUniqueId(), ChatMode.FACTION);
            player.sendMessage("§aChat faction activé.");
            return true;
        }

        if (label.equalsIgnoreCase("fa")) {
            chatModes.setMode(player.getUniqueId(), ChatMode.ALLIANCE);
            player.sendMessage("§aChat alliance activé.");
            return true;
        }

        if (label.equalsIgnoreCase("fg")) {
            chatModes.setMode(player.getUniqueId(), ChatMode.GLOBAL);
            player.sendMessage("§aChat global activé.");
            return true;
        }

        return true;
    }
}
