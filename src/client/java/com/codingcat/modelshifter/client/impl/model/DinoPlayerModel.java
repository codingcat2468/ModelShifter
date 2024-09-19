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

public class DinoPlayerModel extends PlayerModel {
    public DinoPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "dino_player"), Set.of("Domplanto"), new FeatureRendererStates(
                true,
                false,
                true,
                true,
                true,
                true,
                false,
                true
        ), new GuiRenderInfo()
                .setShowcaseRenderTweakFunction(DinoPlayerModel::modifyGuiRendering)
                .setButtonRenderTweakFunction(DinoPlayerModel::modifyGuiRendering));
    }

    private static void modifyGuiRendering(MatrixStack matrixStack) {
        matrixStack.scale(0.5f, 0.5f, 0.5f);
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {}

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.PI * 0.5f);
        matrixStack.multiply(quaternionf);
        matrixStack.translate(0f, -0.4f, -0.2f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f, 0f, -0.2f);
    }
}
