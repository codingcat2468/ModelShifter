package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.state.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public interface PlayerModel {
    Identifier getModelDataIdentifier();

    void modifyHeldItemRendering(MatrixStack matrixStack);

    void modifyElytraRendering(MatrixStack matrixStack);

    @NotNull DisabledFeatureRenderers getDisabledFeatureRenderers();
}
