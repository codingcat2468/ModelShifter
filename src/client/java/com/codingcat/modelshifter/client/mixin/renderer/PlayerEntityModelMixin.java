package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> {
    @Unique
    private static final FeatureRendererType TYPE_CAPE = FeatureRendererType.CAPE;
    @Unique
    private static final FeatureRendererType TYPE_DEADMAU5_EARS = FeatureRendererType.DEADMAU5_EARS;

    @Shadow @Final private ModelPart cloak;

    @Shadow @Final private ModelPart ear;

    @Inject(at = @At(value = "HEAD"),
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    public void insertVisibility(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!(livingEntity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        FeatureRendererStates states = ModelShifterClient.state.accessFeatureRendererStates(clientPlayer);
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;

        if (states.isRendererEnabled(TYPE_CAPE))
            this.cloak.visible = true;
        if (states.isRendererEnabled(TYPE_DEADMAU5_EARS))
            this.ear.visible = true;
    }
}
