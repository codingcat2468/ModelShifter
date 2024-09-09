package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StuckStingersFeatureRenderer.class)
public class StuckStingersFeatureRendererMixin<T extends LivingEntity> {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStingerCount()I"),
            method = "getObjectCount")
    public int insertObjectCount(T instance) {
        if (!(instance instanceof AbstractClientPlayerEntity clientPlayer)) return instance.getStingerCount();
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)
                || ModelShifterClient.state.accessDisabledFeatureRenderers(clientPlayer).disableStuckStingers())
            return instance.getStingerCount();

        return 0;
    }
}
