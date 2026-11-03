package com.crow.crowculling;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

public class PlayerFilter {
    public static final Map<UUID, Long> TRACKED = new ConcurrentHashMap<>();
    public static final long KEEP_MS = 15000;

    private static volatile long combatUntil = 0;
    private static Integer originalViewDistance = null;

    public static void track(Entity e) {
        if (e instanceof OtherClientPlayerEntity) {
            long until = System.currentTimeMillis() + KEEP_MS;
            TRACKED.put(e.getUuid(), until);
            combatUntil = until;
            MinecraftClient.getInstance().execute(PlayerFilter::applyReducedDistance);
        }
    }

    public static boolean shouldHide(Entity e) {
        if (!(e instanceof OtherClientPlayerEntity)) return false;

        if (System.currentTimeMillis() >= combatUntil) {
            if (!TRACKED.isEmpty()) TRACKED.clear();
            return false;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return false;

        double range = CrowConfig.INSTANCE.range;
        if (mc.player.squaredDistanceTo(e) <= range * range && range > 0) return false;

        Long until = TRACKED.get(e.getUuid());
        if (until != null) {
            if (System.currentTimeMillis() < until) return false;
            TRACKED.remove(e.getUuid());
        }
        return true;
    }

    private static void applyReducedDistance() {
        if (!CrowConfig.INSTANCE.reduceRenderDistance) return;
        var opt = MinecraftClient.getInstance().options.getViewDistance();
        int target = CrowConfig.INSTANCE.combatRenderDistance;
        if (originalViewDistance == null) {
            int cur = opt.getValue();
            if (cur <= target) return;
            originalViewDistance = cur;
        }
        if (opt.getValue() != target) opt.setValue(target);
    }

    public static void restoreDistance() {
        if (originalViewDistance == null) return;
        MinecraftClient.getInstance().options.getViewDistance().setValue(originalViewDistance);
        originalViewDistance = null;
    }

    public static void tick() {
        if (originalViewDistance != null
                && (!CrowConfig.INSTANCE.reduceRenderDistance
                    || System.currentTimeMillis() >= combatUntil)) {
            restoreDistance();
        }
    }

    public static void reset() {
        restoreDistance();
        combatUntil = 0;
        TRACKED.clear();
    }
}
