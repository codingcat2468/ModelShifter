package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StuckArrowsFeatureRenderer.class)
public class StuckArrowsFeatureRendererMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStuckArrowCount()I"),
            method = "getObjectCount")
    public int insertObjectCount(LivingEntity instance) {
        if (!ModelShifterClient.additionalRendererState.rendererEnabled().get()
                || ModelShifterClient.additionalRendererState.getDisabledFeatureRenderers().disableStuckArrows())
            return instance.getStuckArrowCount();

        return 0;
    }
}
