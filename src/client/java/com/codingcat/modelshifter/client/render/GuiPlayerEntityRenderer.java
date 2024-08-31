package com.codingcat.modelshifter.client.render;

import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import com.codingcat.modelshifter.client.render.model.GuiPlayerModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class GuiPlayerEntityRenderer extends GeoObjectRenderer<ReplacedPlayerEntity> {
    private final ReplacedPlayerEntity replacedPlayerEntity;
    public GuiPlayerEntityRenderer(Identifier modelIdentifier) {
        super(new GuiPlayerModel(modelIdentifier));
        this.replacedPlayerEntity = new ReplacedPlayerEntity(true);
    }

    public void render(Identifier skin, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.animatable = replacedPlayerEntity;
        RenderLayer type = getRenderType(animatable, skin, vertexConsumerProvider, g);
        defaultRender(matrixStack, animatable, vertexConsumerProvider, type, null, f, g, i);
        this.animatable = null;
    }

    @Override
    public void preRender(MatrixStack poseStack, ReplacedPlayerEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.objectRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
}
