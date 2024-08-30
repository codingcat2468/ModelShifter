package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererHolder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Shadow
    public abstract Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity);

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        ModelShifterClient.holder = new AdditionalRendererHolder(ctx, ModelShifterClient.state);
        ModelShifterClient.holder.applyState();
    }

    @Inject(at = @At("HEAD"),
            method = "setModelPose",
            cancellable = true)
    public void injectSetModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled()) return;

        this.getModel().setVisible(false);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void render(AbstractClientPlayerEntity clientPlayer, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!ModelShifterClient.state.isRendererEnabled()
                || clientPlayer.isSpectator()) return;

        if (ModelShifterClient.holder.getRenderer() != null)
            ModelShifterClient.holder.getRenderer().render(clientPlayer, getTexture(clientPlayer), g, g, matrixStack, vertexConsumerProvider, i);
    }
}
