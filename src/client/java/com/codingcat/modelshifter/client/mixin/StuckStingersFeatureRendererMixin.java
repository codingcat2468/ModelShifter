package com.codingcat.modelshifter.client.mixin;

import com.codingcat.modelshifter.client.ModelShifterClient;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StuckStingersFeatureRenderer.class)
public class StuckStingersFeatureRendererMixin<T extends LivingEntity, M extends PlayerEntityModel<T>> {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStingerCount()I"),
            method = "getObjectCount")
    public int insertObjectCount(T instance) {
        if (!ModelShifterClient.additionalRendererState.rendererEnabled().get()
                || ModelShifterClient.additionalRendererState.getDisabledFeatureRenderers().disableStuckStingers())
            return instance.getStingerCount();

        return 0;
    }
}
