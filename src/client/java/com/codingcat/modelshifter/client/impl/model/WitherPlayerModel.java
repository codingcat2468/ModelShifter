package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WitherPlayerModel extends PlayerModel {
    public WitherPlayerModel() {
        super(new Identifier(ModelShifterClient.MOD_ID, "wither_player"), new DisabledFeatureRenderers(
                true,
                true,
                false,
                true,
                true,
                true,
                false,
                true
        ));
    }

    @Override
    public void modifyHeldItemRendering(MatrixStack matrixStack) {
        matrixStack.translate(-0.03f,0f,-0.1f);
    }

    @Override
    public void modifyElytraRendering(MatrixStack matrixStack) {}
}
