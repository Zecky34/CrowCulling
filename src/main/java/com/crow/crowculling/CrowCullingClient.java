package com.crow.crowculling;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;

public class CrowCullingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (world.isClient()) PlayerFilter.track(entity);
            return ActionResult.PASS;
        });
    }
}
