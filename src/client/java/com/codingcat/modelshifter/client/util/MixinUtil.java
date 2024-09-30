package com.codingcat.modelshifter.client.util;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

public class MixinUtil {
    public static void ifRendererEnabled(FeatureRendererType type, LivingEntity livingEntity, boolean invertFeatureRenderer, Consumer<FeatureRendererStates> run) {
        if (!(livingEntity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        FeatureRendererStates states = ModelShifterClient.state.accessFeatureRendererStates(clientPlayer);
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)
                || invertFeatureRenderer != states.isRendererEnabled(type)) return;

        run.accept(states);
    }

    public static void insertModifyRendering(FeatureRendererType type, Entity entity, MatrixStack matrixStack, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return;

        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;
        FeatureRendererStates states = ModelShifterClient.state.accessFeatureRendererStates(clientPlayer);
        if (states.isRendererEnabled(type))
            states.modifyRendering(type, clientPlayer, matrixStack);
        else
            ci.cancel();
    }
}
