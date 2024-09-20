package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public abstract class PlayerModel {
    private final Identifier identifier;
    private final Set<String> creators;
    @NotNull
    private final FeatureRendererStates featureRendererStates;
    @NotNull
    private final GuiRenderInfo guiRenderInfo;

    public PlayerModel(Identifier identifier, Set<String> creators) {
        this.identifier = identifier;
        this.creators = creators;
        this.featureRendererStates = this.createFeatureRendererStates();
        this.guiRenderInfo = this.createGuiRenderInfo();
    }

    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo();
    }

    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates();
    }

    public final Identifier getModelDataIdentifier() {
        return this.identifier;
    }

    public final Set<String> getCreators() {
        return this.creators;
    }

    @NotNull
    public final GuiRenderInfo getGuiRenderInfo() {
        return this.guiRenderInfo;
    }

    public final @NotNull FeatureRendererStates getFeatureRendererStates() {
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
