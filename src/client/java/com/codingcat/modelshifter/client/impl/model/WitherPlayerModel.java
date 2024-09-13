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

public class WitherPlayerModel extends PlayerModel {
    public WitherPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "wither_player"), Set.of("bug_finder"), new FeatureRendererStates(
                true,
                true,
                false,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo()
                .setButtonAnimation(DefaultAnimations.IDLE)
                .setButtonRenderTweakFunction(WitherPlayerModel::modifyGuiButtonRendering));
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.translate(0f,-0.15f,0f);
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(-0.03f, 0f, -0.1f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.35f);
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
    }
}
