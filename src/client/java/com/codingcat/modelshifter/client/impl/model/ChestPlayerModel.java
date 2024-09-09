package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public class ChestPlayerModel extends PlayerModel {
    public ChestPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "chest_player"), Set.of("bug_finder"), new DisabledFeatureRenderers(
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo());
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.44f, 0.35f, -0.5f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.05f);
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.scale(0.9f, 0.65f, 0.9f);
        matrixStack.translate(0f, 1f, 0.35f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0.2f,0f);
    }
}
