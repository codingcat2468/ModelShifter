package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ChestPlayerModel extends PlayerModel {
    public ChestPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "chest_player"), Set.of("bug_finder"));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, ChestPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, ChestPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.44f, 0.35f, -0.5f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.05f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.scale(0.9f, 0.65f, 0.9f);
        matrixStack.translate(0f, 1f, 0.35f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0.2f,0f);
    }
}
