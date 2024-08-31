package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Set;

public class AmongUsPlayerModel extends PlayerModel {
    public AmongUsPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "among_us_player"), Set.of("EgertSUS"), new DisabledFeatureRenderers(
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
