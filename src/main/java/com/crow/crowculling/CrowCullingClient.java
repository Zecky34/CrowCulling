package com.crow.crowculling;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class CrowCullingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CrowConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> PlayerFilter.tick());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> PlayerFilter.reset());
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> PlayerFilter.restoreDistance());
    }
}
