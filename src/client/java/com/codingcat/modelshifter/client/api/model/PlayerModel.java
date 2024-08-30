package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.state.DisabledFeatureRenderers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerModel model)) return false;
        return Objects.equals(identifier, model.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
