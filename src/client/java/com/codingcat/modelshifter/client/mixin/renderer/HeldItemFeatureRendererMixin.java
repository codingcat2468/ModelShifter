package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(at = @At(value = "HEAD"),
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            cancellable = true)
    public void insertModifyRendering(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (!(livingEntity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)
                || !ModelShifterClient.state.accessDisabledFeatureRenderers(clientPlayer).disableHeldItem()) return;

        ci.cancel();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/ModelWithArms;setArmAngle(Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "renderItem")
    public void insertModifyRendering(ModelWithArms instance, Arm arm, MatrixStack matrixStack, @Local(argsOnly = true) LivingEntity entity) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        if (ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;

        ((ModelWithArms) this.getContextModel()).setArmAngle(arm, matrixStack);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"), method = "renderItem")
    public void insertModifyRendering(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;
        PlayerModel model = ModelShifterClient.state.getState(clientPlayer.getUuid()).getPlayerModel();

        if (model != null)
            model.modifyHeldItemRendering(entity, matrices);
    }
}
