package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public class ArmorStandPlayerModel extends PlayerModel {
    public ArmorStandPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "armor_stand_player"), Set.of("bug_finder"), new FeatureRendererStates(
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo()
                .setButtonRenderTweakFunction(ArmorStandPlayerModel::modifyGuiButtonRendering)
                .setShowcaseRenderTweakFunction(ArmorStandPlayerModel::modifyGuiShowcaseRendering));
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.9f,0.9f,0.9f);
    }

    private static void modifyGuiShowcaseRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.9f,0.9f,0.9f);
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.35f, 0.1f, 0.05f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.7f);
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0.4f, 0f);
    }
}
