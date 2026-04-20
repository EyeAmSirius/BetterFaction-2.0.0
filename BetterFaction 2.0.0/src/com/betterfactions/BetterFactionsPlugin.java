package com.betterfactions;

import com.betterfactions.commands.*;
import com.betterfactions.core.repository.FactionRelationsRepository;
import com.betterfactions.core.repository.FactionRepository;
import com.betterfactions.core.repository.YamlFactionRelationsRepository;
import com.betterfactions.core.repository.YamlFactionRepository;
import com.betterfactions.core.service.FactionService;
import com.betterfactions.core.service.FactionServiceImpl;
import com.betterfactions.modules.chat.ChatModeManager;
import com.betterfactions.modules.chat.FactionAdvancedChatListener;
import com.betterfactions.modules.claims.*;
import com.betterfactions.modules.map.FactionMapListener;
import com.betterfactions.modules.power.PowerListener;
import com.betterfactions.modules.power.PowerRepository;
import com.betterfactions.modules.power.YamlPowerRepository;
import com.betterfactions.modules.raid.*;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterFactionsPlugin extends JavaPlugin {

    private FactionService factionService;
    private ClaimService claimService;
    private RaidService raidService;
    private ChatModeManager chatModes;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        // -------------------------
        // Factions + Relations (YAML)
        // -------------------------
        FactionRepository factionRepo = new YamlFactionRepository(getDataFolder());
        FactionRelationsRepository relationsRepo = new YamlFactionRelationsRepository(getDataFolder());
        factionService = new FactionServiceImpl(factionRepo, relationsRepo);

        // -------------------------
        // Claims (YAML)
        // -------------------------
        ClaimsRepository claimsRepo = new YamlClaimsRepository(getDataFolder());
        claimService = new ClaimServiceYamlImpl(claimsRepo);

        // -------------------------
        // Raids (YAML)
        // -------------------------
        RaidRepository raidRepo = new YamlRaidRepository(getDataFolder());
        raidService = new RaidServiceYamlImpl(raidRepo);

        // -------------------------
        // Power (YAML)
        // -------------------------
        PowerRepository powerRepo = new YamlPowerRepository(getDataFolder());
        // PowerListener pourra utiliser powerRepo si tu veux un vrai système de power

        // -------------------------
        // Chat modes
        // -------------------------
        chatModes = new ChatModeManager();

        // -------------------------
        // Commandes
        // -------------------------
        getCommand("faction").setExecutor(new FactionCommand(factionService));
        getCommand("fclaim").setExecutor(new FactionClaimCommand(factionService, claimService));
        getCommand("fmap").setExecutor(new FactionMapCommand(claimService, factionService));
        getCommand("fraid").setExecutor(new FactionRaidCommand(factionService, raidService));

        getCommand("fc").setExecutor(new FactionChatModeCommand(chatModes));
        getCommand("fa").setExecutor(new FactionChatModeCommand(chatModes));
        getCommand("fg").setExecutor(new FactionChatModeCommand(chatModes));

        // 🔥 Commande admin
        getCommand("factionadmin").setExecutor(new AdminFactionCommand(factionService));

        // -------------------------
        // Listeners
        // -------------------------
        getServer().getPluginManager().registerEvents(
                new FactionAdvancedChatListener(factionService, chatModes), this
        );

        getServer().getPluginManager().registerEvents(
                new FactionMapListener(claimService), this
        );

        getServer().getPluginManager().registerEvents(
                new PowerListener(factionService), this
        );

        getServer().getPluginManager().registerEvents(
                new ClaimProtectionListener(claimService, factionService), this
        );

        getServer().getPluginManager().registerEvents(
                new RaidProtectionListener(raidService), this
        );

        getLogger().info("BetterFactions (Version B + YAML) chargé.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterFactions désactivé.");
        // Tous les repos YAML sauvegardent automatiquement à chaque modification
    }
}