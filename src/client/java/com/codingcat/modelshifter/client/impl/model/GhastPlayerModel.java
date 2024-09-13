package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public class GhastPlayerModel extends PlayerModel {
    public GhastPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "ghast_player"), Set.of("bug_finder"), new FeatureRendererStates(
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true
        ), new GuiRenderInfo()
                .setButtonRenderTweakFunction(GhastPlayerModel::modifyGuiButtonRendering));
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.translate(0f,-0.16f,0f);
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {}

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {}
}
