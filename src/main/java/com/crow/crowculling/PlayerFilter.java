package com.crow.crowculling;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

public class PlayerFilter {
    public static final Map<UUID, Long> TRACKED = new ConcurrentHashMap<>();
    public static final double RANGE = 15;
    public static final long KEEP_MS = 15000;

    // Savaş modunun bitiş zamanı
    private static volatile long combatUntil = 0;

    public static void track(Entity e) {
        if (e instanceof OtherClientPlayerEntity) {
            long until = System.currentTimeMillis() + KEEP_MS;
            TRACKED.put(e.getUuid(), until);
            combatUntil = until;
        }
    }

    public static boolean shouldHide(Entity e) {
        if (!(e instanceof OtherClientPlayerEntity)) return false;

        // Savaş modu kapalıysa hiçbir şey gizlenmez
        if (System.currentTimeMillis() >= combatUntil) {
            if (!TRACKED.isEmpty()) TRACKED.clear();
            return false;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return false;
        if (mc.player.squaredDistanceTo(e) <= RANGE * RANGE) return false;

        Long until = TRACKED.get(e.getUuid());
        if (until != null) {
            if (System.currentTimeMillis() < until) return false;
            TRACKED.remove(e.getUuid());
        }
        return true;
    }
}
