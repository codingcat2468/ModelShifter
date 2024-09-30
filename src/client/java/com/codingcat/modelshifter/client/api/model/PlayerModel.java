package com.codingcat.modelshifter.client.api.model;

import com.codingcat.modelshifter.client.api.animation.ModelAnimationController;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Objects;
import java.util.Set;

public abstract class PlayerModel {
    private final Identifier identifier;
    private final Set<String> creators;
    @NotNull
    private final FeatureRendererStates featureRendererStates;
    @NotNull
    private final GuiRenderInfo guiRenderInfo;
    @NotNull
    private final ModelAnimationController<PlayerEntity> animationController;
    @NotNull
    private final ModelDimensions dimensions;

    public PlayerModel(Identifier identifier, Set<String> creators, @NotNull ModelDimensions dimensions) {
        this.identifier = identifier;
        this.creators = creators;
        this.dimensions = dimensions;
        this.featureRendererStates = this.createFeatureRendererStates();
        this.guiRenderInfo = this.createGuiRenderInfo();
        this.animationController = this.createAnimationController();
    }

    protected @NotNull ModelAnimationController<PlayerEntity> createAnimationController() {
        return ModelAnimationController.createDefaultController();
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
    public final ModelDimensions getDimensions() {
        return this.dimensions;
    }

    @Nullable
    public final RawAnimation getCurrentAnimation(PlayerEntity entity) {
        return this.animationController.getAnimation(entity);
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
