package com.crow.crowculling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class CrowConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("crowculling.json");

    public static CrowConfig INSTANCE = new CrowConfig();

    public int range = 15;                    // 0-25
    public boolean reduceRenderDistance = false;
    public int combatRenderDistance = 5;      // 2-16 chunk

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                CrowConfig c = GSON.fromJson(Files.readString(FILE), CrowConfig.class);
                if (c != null) INSTANCE = c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        INSTANCE.range = Math.max(0, Math.min(25, INSTANCE.range));
        INSTANCE.combatRenderDistance = Math.max(2, Math.min(16, INSTANCE.combatRenderDistance));
        save();
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
