package com.crow.crowculling.mixin;

import com.crow.crowculling.PlayerFilter;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class RenderMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void crowculling$hide(E entity, Frustum frustum,
            double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerFilter.shouldHide(entity)) cir.setReturnValue(false);
    }
}
