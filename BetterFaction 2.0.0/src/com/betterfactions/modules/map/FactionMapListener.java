package com.betterfactions.modules.map;

import com.betterfactions.modules.claims.ClaimService;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FactionMapListener implements Listener {

    private final ClaimService claimService;

    public FactionMapListener(ClaimService claimService) {
        this.claimService = claimService;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (e.getFrom().getChunk().equals(e.getTo().getChunk())) return;

        Player player = e.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        claimService.getFactionByChunk(chunk).ifPresentOrElse(factionName -> {

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new TextComponent("§6Territoire : §e" + factionName)
            );

        }, () -> player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent("§7Territoire : §fWilderness")
        ));
    }
}