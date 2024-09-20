package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FlatPlayerModel extends PlayerModel {
    public FlatPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "2d_player"), Set.of("bug_finder"));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, FlatPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, FlatPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    @Override
    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo()
                .setButtonRenderTweakFunction(FlatPlayerModel::modifyGuiButtonRendering)
                .setShowcaseRenderTweakFunction(FlatPlayerModel::modifyGuiShowcaseRendering);
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.8f, 0.8f, 0.8f);
    }

    private static void modifyGuiShowcaseRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.9f, 0.9f, 0.9f);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.35f,0f,0.05f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0.05f,-0.2f,-0.3f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0f,0f,0f);
    }
}
