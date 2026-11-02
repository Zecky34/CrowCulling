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
        if (mc.player == null || mc.world == null) return;
        if (packet.entityId() != mc.player.getId()) return;
        Entity attacker = mc.world.getEntityById(packet.sourceCauseId());
        if (attacker != null) PlayerFilter.track(attacker);
    }
}
