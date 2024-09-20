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

public class EndermanPlayerModel extends PlayerModel {
    public EndermanPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "enderman_player"), Set.of("bug_finder"));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, EndermanPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, EndermanPlayerModel::modifyElytraRendering);
    }

    @Override
    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo()
                .setButtonRenderTweakFunction(EndermanPlayerModel::modifyGuiButtonRendering)
                .setShowcaseRenderTweakFunction(EndermanPlayerModel::modifyGuiShowcaseRendering);
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.55f, 0.55f, 0.55f);
    }

    private static void modifyGuiShowcaseRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.6f, 0.6f, 0.6f);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.35f, 0f, 1f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.7f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0f, -1.2f, -0.1f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0.5f, 0f);
    }
}
