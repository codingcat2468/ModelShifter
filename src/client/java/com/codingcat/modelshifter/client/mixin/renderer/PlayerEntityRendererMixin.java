package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DynamicAdditionalRendererHolder;
import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Shadow
    public abstract Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity);

    @Shadow
    protected abstract void setupTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, float i);

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        ModelShifterClient.holder = new DynamicAdditionalRendererHolder(ctx, ModelShifterClient.state);
        ModelShifterClient.holder.applyState();
    }

    @Inject(at = @At("HEAD"),
            method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;",
            cancellable = true)
    public void injectSetModelPose(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, CallbackInfoReturnable<Vec3d> cir) {
        if (!ModelShifterClient.state.isRendererEnabled(abstractClientPlayerEntity)) return;

        cir.setReturnValue(super.getPositionOffset(abstractClientPlayerEntity, f));
        cir.cancel();
    }

    @Inject(at = @At("HEAD"),
            method = "setModelPose",
            cancellable = true)
    public void injectSetModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled(player)) return;

        this.getModel().setVisible(false);
        if (player.isSpectator())
            this.model.head.visible = true;

        ci.cancel();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"),
            method = "renderArm")
    public void injectRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        if (arm == model.leftArm) {
            model.leftArm.visible = true;
            model.leftSleeve.visible = true;
        }
        if (arm == model.rightArm) {
            model.rightArm.visible = true;
            model.rightSleeve.visible = true;
        }
    }


    @Inject(at = @At("HEAD"),
            method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V",
            cancellable = true)
    public void injectSetupTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, float i, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled(abstractClientPlayerEntity)) return;

        super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h, i);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void render(AbstractClientPlayerEntity clientPlayer, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)
                || clientPlayer.isSpectator()) return;

        PlayerModel playerModel = ModelShifterClient.state.getState(clientPlayer.getUuid()).getPlayerModel();
        ReplacedPlayerEntityRenderer renderer = ModelShifterClient.holder.getRenderer(playerModel);
        if (renderer != null)
            renderer.render(clientPlayer, getTexture(clientPlayer), g, g, matrixStack, vertexConsumerProvider, i);
    }
}
