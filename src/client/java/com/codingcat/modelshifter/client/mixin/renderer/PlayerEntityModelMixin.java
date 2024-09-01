package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> {
    @Shadow @Final private ModelPart cloak;

    @Shadow @Final private ModelPart ear;

    @Inject(at = @At(value = "HEAD"),
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    public void insertVisibility(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled()) return;

        DisabledFeatureRenderers renderers = ModelShifterClient.state.accessDisabledFeatureRenderers();
        if (!renderers.disableCape())
            this.cloak.visible = true;
        if (!renderers.disableDeadmau5Ears())
            this.ear.visible = true;
    }
}
