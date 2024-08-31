package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AmongUsPlayerModel extends PlayerModel {
    public AmongUsPlayerModel() {
        super(new Identifier(ModelShifterClient.MOD_ID, "among_us_player"), new DisabledFeatureRenderers(
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                true
        ));
    }

    @Override
    public void modifyHeldItemRendering(MatrixStack matrixStack) {
        matrixStack.translate(0.3f,0.2f,0f);
    }

    @Override
    public void modifyElytraRendering(MatrixStack matrixStack) {}
}
