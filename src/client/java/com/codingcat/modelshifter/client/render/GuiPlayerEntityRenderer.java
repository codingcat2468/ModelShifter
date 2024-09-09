package com.codingcat.modelshifter.client.render;

import com.codingcat.modelshifter.client.render.entity.ReplacedPlayerEntity;
import com.codingcat.modelshifter.client.render.model.GuiPlayerModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.util.Color;

public class GuiPlayerEntityRenderer extends GeoObjectRenderer<ReplacedPlayerEntity> {
    private final ReplacedPlayerEntity replacedPlayerEntity;
    private Color renderColor;

    public GuiPlayerEntityRenderer(Identifier modelIdentifier, @NotNull RawAnimation animation) {
        super(new GuiPlayerModel(modelIdentifier));
        this.replacedPlayerEntity = new ReplacedPlayerEntity(animation, true);
        this.renderColor = Color.WHITE;
    }

    @Override
    public Color getRenderColor(ReplacedPlayerEntity animatable, float partialTick, int packedLight) {
        return this.renderColor;
    }

    public void setRenderColor(int r, int g, int b, int a) {
        this.renderColor = Color.ofRGBA(r, g, b, a);
    }

    public void render(Identifier skin, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.animatable = replacedPlayerEntity;
        RenderLayer type = getRenderType(animatable, skin, vertexConsumerProvider, g);
        defaultRender(matrixStack, animatable, vertexConsumerProvider, type, null, f, g, i);
        this.animatable = null;
    }

    //? <1.21 {
    /*@Override
    public void preRender(MatrixStack poseStack, ReplacedPlayerEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.objectRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
    *///?} else {
    @Override
    public void preRender(MatrixStack poseStack, ReplacedPlayerEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        this.objectRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }
    //?}
}
