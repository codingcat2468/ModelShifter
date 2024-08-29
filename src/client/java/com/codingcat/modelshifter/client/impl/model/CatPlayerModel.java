package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.state.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class CatPlayerModel implements PlayerModel {
    @Override
    public Identifier getModelDataIdentifier() {
        return new Identifier(ModelShifterClient.MOD_ID, "cat_player");
    }

    @Override
    public void modifyHeldItemRendering(MatrixStack matrixStack) {

    }

    @Override
    public void modifyElytraRendering(MatrixStack matrixStack) {

    }

    @Override
    public @NotNull DisabledFeatureRenderers getDisabledFeatureRenderers() {
        return new DisabledFeatureRenderers(
                true,
                false,
                false,
                true,
                true,
                true,
                false,
                true
        );
    }
}
