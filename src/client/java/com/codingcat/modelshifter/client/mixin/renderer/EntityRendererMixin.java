package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(
            //? >1.20.4 {
            method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V")
            //?} else {
            /*method = "renderLabelIfPresent",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V")
            *///?}
    )
    protected void injectLabelPositionOffset(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                             //? >1.20.4 {
                                             float tickDelta,
                                             //?}
                                             CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return;
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return;
        PlayerModel model = ModelShifterClient.state.getState(clientPlayer.getUuid()).getPlayerModel();
        if (model == null) return;

        matrices.translate(0f, model.getDimensions().labelOffset(), 0f);
    }
}
