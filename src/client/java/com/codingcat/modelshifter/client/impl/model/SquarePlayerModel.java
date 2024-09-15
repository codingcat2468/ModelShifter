package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.Set;

public class SquarePlayerModel extends PlayerModel {
    public SquarePlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "square_player"), Set.of("bug_finder"), new FeatureRendererStates(
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo()
                .setButtonAnimation(DefaultAnimations.WALK));
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.5f, 0.35f, -0.5f);
        if (entity.isInSneakingPose()) {
            matrixStack.scale(1f, 1f, 0.55f);
            matrixStack.translate(0f,0f,-0.8f);
        }
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.scale(1.1f, 0.65f, 0.9f);
        matrixStack.translate(0f, 1f, 0.35f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0.2f,0f);
    }
}
