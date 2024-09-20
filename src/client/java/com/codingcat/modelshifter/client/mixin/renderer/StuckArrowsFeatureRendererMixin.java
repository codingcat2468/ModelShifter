package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StuckArrowsFeatureRenderer.class)
public class StuckArrowsFeatureRendererMixin {
    @Unique
    private static final FeatureRendererType TYPE = FeatureRendererType.STUCK_ARROWS;

    @Inject(at = @At("HEAD"),
            method = "renderObject",
            cancellable = true)
    public void injectModifyRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float directionX, float directionY, float directionZ, float tickDelta, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        FeatureRendererStates states = ModelShifterClient.state.accessFeatureRendererStates(clientPlayer);
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;

        if (states.isRendererEnabled(TYPE))
            states.modifyRendering(TYPE, clientPlayer, matrices);
        else
            ci.cancel();
    }
}
