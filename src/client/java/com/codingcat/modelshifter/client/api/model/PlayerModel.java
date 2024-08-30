package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.state.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerModel {
    private final Identifier identifier;
    private final DisabledFeatureRenderers disabledFeatureRenderers;

    public PlayerModel(Identifier identifier, DisabledFeatureRenderers disabledFeatureRenderers) {
        this.identifier = identifier;
        this.disabledFeatureRenderers = disabledFeatureRenderers;
    }

    public abstract void modifyHeldItemRendering(MatrixStack matrixStack);

    public abstract void modifyElytraRendering(MatrixStack matrixStack);

    public Identifier getModelDataIdentifier() {
        return this.identifier;
    }

    public @NotNull DisabledFeatureRenderers getDisabledFeatureRenderers() {
        return this.disabledFeatureRenderers;
    }
}
