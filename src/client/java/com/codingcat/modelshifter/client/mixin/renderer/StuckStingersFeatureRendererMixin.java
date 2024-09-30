package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import com.codingcat.modelshifter.client.util.MixinUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.StuckStingersFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckStingersFeatureRenderer.class)
public class StuckStingersFeatureRendererMixin {
    @Unique
    private static final FeatureRendererType TYPE = FeatureRendererType.STUCK_STINGERS;

    @Inject(at = @At("HEAD"),
            method = "renderObject",
            cancellable = true)
    public void injectModifyRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta, CallbackInfo ci) {
        MixinUtil.insertModifyRendering(TYPE, entity, matrices, ci);
    }
}
