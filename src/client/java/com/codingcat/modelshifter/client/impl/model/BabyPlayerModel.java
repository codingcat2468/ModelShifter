package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.ModelDimensions;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import com.codingcat.modelshifter.client.impl.Creators;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BabyPlayerModel extends PlayerModel {
    public BabyPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "baby_player"), Set.of(Creators.DOMPLANTO),
                new ModelDimensions(0.5f, 1.2f, -0.6f));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, BabyPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, BabyPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.25f,0f,-0.4f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.1f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.scale(0.6f, 0.6f, 0.6f);
        matrixStack.translate(0f, 1.2f, 0f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,-0.2f,0f);
    }
}
