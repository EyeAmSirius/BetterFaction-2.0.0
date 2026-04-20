package com.betterfactions.modules.chat;

import com.betterfactions.core.domain.Faction;
import com.betterfactions.core.service.FactionService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class FactionAdvancedChatListener implements Listener {

    private final FactionService factionService;
    private final ChatModeManager chatModes;

    public FactionAdvancedChatListener(FactionService factionService, ChatModeManager chatModes) {
        this.factionService = factionService;
        this.chatModes = chatModes;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        ChatMode mode = chatModes.getMode(uuid);

        switch (mode) {

            case GLOBAL -> {
                factionService.getFactionByPlayer(uuid).ifPresentOrElse(faction -> {
                    e.setFormat("§7[" + faction.getName() + "§7] §f" + player.getName() + "§7: §f" + e.getMessage());
                }, () -> {
                    e.setFormat("§a[§aWilderness§a] §f" + player.getName() + "§7: §f" + e.getMessage());
                });
            }

            case FACTION -> {
                e.setCancelled(true);

                factionService.getFactionByPlayer(uuid).ifPresentOrElse(faction -> {

                    for (UUID memberId : faction.getMembers().keySet()) {
                        Player p = Bukkit.getPlayer(memberId);
                        if (p != null) {
                            p.sendMessage("§a[Faction] §6" + player.getName() + "§7: §f" + e.getMessage());
                        }
                    }

                }, () -> player.sendMessage("§cTu n'as pas de faction."));
            }

            case ALLIANCE -> {
                e.setCancelled(true);

                factionService.getFactionByPlayer(uuid).ifPresentOrElse(faction -> {

                    // Faction elle-même
                    for (UUID memberId : faction.getMembers().keySet()) {
                        Player p = Bukkit.getPlayer(memberId);
                        if (p != null) {
                            p.sendMessage("§b[Alliance] §6" + player.getName() + "§7: §f" + e.getMessage());
                        }
                    }

                    // Alliés
                    factionService.getAllies(faction.getName()).forEach(allyName -> {
                        factionService.getFactionByName(allyName).ifPresent(allyFaction -> {
                            for (UUID memberId : allyFaction.getMembers().keySet()) {
                                Player p = Bukkit.getPlayer(memberId);
                                if (p != null) {
                                    p.sendMessage("§b[Alliance] §6" + player.getName() + "§7: §f" + e.getMessage());
                                }
                            }
                        });
                    });

                }, () -> player.sendMessage("§cTu n'as pas de faction."));
            }
        }
    }
}