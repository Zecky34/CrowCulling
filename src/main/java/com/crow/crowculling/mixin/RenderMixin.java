package com.crow.crowculling.mixin;

import com.crow.crowculling.CrowConfig;
import com.crow.crowculling.PlayerFilter;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class RenderMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void crowculling$hide(Entity entity, Frustum frustum,
            double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!CrowConfig.INSTANCE.hidePlayers) return;
        if (PlayerFilter.shouldHide(entity)) cir.setReturnValue(false);
    }
}
