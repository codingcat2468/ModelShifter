package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.renderer.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public abstract class PlayerModel {
    private final Identifier identifier;
    private final Set<String> creators;
    private final FeatureRendererStates featureRendererStates;
    @NotNull
    private final GuiRenderInfo guiRenderInfo;

    public PlayerModel(Identifier identifier, Set<String> creators, FeatureRendererStates featureRendererStates, @NotNull GuiRenderInfo guiRenderInfo) {
        this.identifier = identifier;
        this.creators = creators;
        this.featureRendererStates = featureRendererStates;
        this.guiRenderInfo = guiRenderInfo;
    }

    public abstract void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack);

    public abstract void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack);

    public Identifier getModelDataIdentifier() {
        return this.identifier;
    }

    public Set<String> getCreators() {
        return this.creators;
    }

    @NotNull
    public GuiRenderInfo getGuiRenderInfo() {
        return this.guiRenderInfo;
    }

    public @NotNull FeatureRendererStates getDisabledFeatureRenderers() {
        return this.featureRendererStates;
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
