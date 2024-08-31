package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ChestPlayerModel extends PlayerModel {
    public ChestPlayerModel() {
        super(new Identifier(ModelShifterClient.MOD_ID, "chest_player"), new DisabledFeatureRenderers(
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
    public void modifyHeldItemRendering(MatrixStack matrixStack) {}

    @Override
    public void modifyElytraRendering(MatrixStack matrixStack) {}
}
