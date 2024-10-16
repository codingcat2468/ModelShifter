package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.ModelDimensions;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import com.codingcat.modelshifter.client.impl.Creators;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Set;

public class CrabPlayerModel extends PlayerModel {
    public CrabPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "crab_player"), Set.of(Creators.BUG),
                new ModelDimensions(0.7f, 0.7f, -1.2f));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, CrabPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, CrabPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    @Override
    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo()
                .setButtonRenderTweakFunction(CrabPlayerModel::modifyGuiButtonRendering);
    }
    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(1.15f, 1.15f, 1.15f);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI * -0.2f);
        matrixStack.translate(-0.4f, 0.8f, -0.7f);
        matrixStack.multiply(quaternionf);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.1f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.PI * 0.5f);
        matrixStack.multiply(quaternionf);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        matrixStack.translate(0f, -0.3f, -1.2f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, -0.2f, 0f);
    }
}
