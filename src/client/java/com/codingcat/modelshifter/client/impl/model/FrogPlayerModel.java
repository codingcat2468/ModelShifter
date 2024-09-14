package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

import java.util.Set;

public class FrogPlayerModel extends PlayerModel {
    public FrogPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "frog_player"), Set.of("bug_finder"), new FeatureRendererStates(
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo()
                .setButtonRenderTweakFunction(FrogPlayerModel::modifyGuiButtonRendering)
                .setShowcaseRenderTweakFunction(FrogPlayerModel::modifyGuiShowcaseRendering));
    }

    private static void modifyGuiShowcaseRendering(MatrixStack matrixStack) {
        matrixStack.scale(1.3f,1.3f,1.3f);
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(1.6f,1.6f,1.6f);
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.2f,0.2f,-0.7f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.1f);
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.PI * 0.5f);
        matrixStack.multiply(quaternionf);
        matrixStack.scale(0.6f, 0.6f, 0.6f);
        matrixStack.translate(0f, 0.2f, -2f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,-0.2f,0f);
    }
}
