package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.ModelDimensions;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import com.codingcat.modelshifter.client.impl.Creators;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ArmorStandPlayerModel extends PlayerModel {
    public ArmorStandPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "armor_stand_player"), Set.of(Creators.BUG),
                new ModelDimensions(0.7f, 2f));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, ArmorStandPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, ArmorStandPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    @Override
    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo()
                .setButtonRenderTweakFunction(ArmorStandPlayerModel::modifyGuiButtonRendering)
                .setShowcaseRenderTweakFunction(ArmorStandPlayerModel::modifyGuiShowcaseRendering);
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.9f,0.9f,0.9f);
    }

    private static void modifyGuiShowcaseRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.9f,0.9f,0.9f);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.35f, 0.1f, 0.05f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.7f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0.4f, 0f);
    }
}
