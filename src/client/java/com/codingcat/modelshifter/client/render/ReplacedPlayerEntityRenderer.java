package com.codingcat.modelshifter.client.render;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class ReplacedPlayerEntityRenderer extends GeoReplacedEntityRenderer<AbstractClientPlayerEntity, ReplacedPlayerEntity> {
    private final Identifier modelIdentifier;
    public ReplacedPlayerEntityRenderer(EntityRendererFactory.Context renderManager, Identifier modelIdentifier) {
        super(renderManager,
                new DefaultedEntityGeoModel<ReplacedPlayerEntity>(modelIdentifier).withAltTexture(ModelShifterClient.EMPTY_TEXTURE),
                new ReplacedPlayerEntity(null, false));
        this.modelIdentifier = modelIdentifier;
    }

    public Identifier getModelIdentifier() {
        return this.modelIdentifier;
    }

    public void render(AbstractClientPlayerEntity clientPlayer, Identifier skin, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.currentEntity = clientPlayer;
        RenderLayer type = getRenderType(animatable, skin, vertexConsumerProvider, g);
        defaultRender(matrixStack, animatable, vertexConsumerProvider, type, null, f, g, i);
        this.currentEntity = null;
    }
}
