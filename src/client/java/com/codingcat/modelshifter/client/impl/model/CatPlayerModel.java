package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import com.codingcat.modelshifter.client.impl.Creators;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Set;

public class CatPlayerModel extends PlayerModel {
    public CatPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "cat_player"), Set.of(Creators.EGBERT));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, CatPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, CatPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.1f, 0.1f, -0.5f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0,0,-0.1);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.PI * 0.5f);
        matrixStack.multiply(quaternionf);
        matrixStack.scale(0.7f, 0.7f, 0.7f);
        matrixStack.translate(0f, -0.4f, -1.3f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.2f);
    }
}
