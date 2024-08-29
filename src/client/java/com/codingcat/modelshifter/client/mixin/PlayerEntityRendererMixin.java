package com.codingcat.modelshifter.client.mixin;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.state.AdditionalRendererState;
import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Shadow
    public abstract Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity);

    @Unique
    private ReplacedPlayerEntityRenderer additionalRenderer;
    @Unique
    private EntityRendererFactory.Context context;

    @Inject(at = @At("RETURN"), method = "<init>")
    public void onInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.context = ctx;
        ModelShifterClient.additionalRendererState = new AdditionalRendererState(new AtomicBoolean(false), new AtomicReference<>(),
                model -> setAdditionalRenderModel(model.getModelDataIdentifier()));
    }

    @Unique
    private void setAdditionalRenderModel(Identifier modelIdentifier) {
        this.additionalRenderer = new ReplacedPlayerEntityRenderer(context, modelIdentifier);
    }

    @Inject(at = @At("HEAD"),
            method = "setModelPose",
            cancellable = true)
    public void injectSetModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        if (!ModelShifterClient.additionalRendererState.rendererEnabled().get()) return;

        this.getModel().setVisible(false);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void render(AbstractClientPlayerEntity clientPlayer, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (!ModelShifterClient.additionalRendererState.rendererEnabled().get()
                || clientPlayer.isSpectator()) return;

        additionalRenderer.render(clientPlayer, getTexture(clientPlayer), g, g, matrixStack, vertexConsumerProvider, i);
    }
}
