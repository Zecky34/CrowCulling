package com.crow.crowculling.mixin;

import com.crow.crowculling.PlayerFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class DamageMixin {
    @Inject(method = "onEntityDamage", at = @At("HEAD"))
    private void crowculling$onDamage(EntityDamageS2CPacket packet, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int victimId = packet.entityId();
        int attackerId = packet.sourceCauseId();

        mc.execute(() -> {
            if (mc.player == null || mc.world == null) return;
            int myId = mc.player.getId();

            if (victimId == myId) {
                // Biz hasar yedik: saldırgan takip edilir
                Entity attacker = mc.world.getEntityById(attackerId);
                if (attacker != null) PlayerFilter.track(attacker);
            } else if (attackerId == myId) {
                // Biz hasar verdik: kurban takip edilir
                Entity victim = mc.world.getEntityById(victimId);
                if (victim != null) PlayerFilter.trackHit(victim);
            }
        });
    }
}
