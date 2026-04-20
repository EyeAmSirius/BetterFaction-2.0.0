package com.betterfactions.modules.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatModeManager {

    private final Map<UUID, ChatMode> modes = new HashMap<>();

    public ChatMode getMode(UUID uuid) {
        return modes.getOrDefault(uuid, ChatMode.GLOBAL);
    }

    public void setMode(UUID uuid, ChatMode mode) {
        modes.put(uuid, mode);
    }
}
