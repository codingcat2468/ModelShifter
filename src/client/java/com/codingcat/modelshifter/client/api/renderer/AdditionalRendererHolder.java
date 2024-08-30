package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdditionalRendererHolder {
    private final EntityRendererFactory.Context context;
    @NotNull
    private final AdditionalRendererState state;
    @Nullable
    private ReplacedPlayerEntityRenderer additionalRenderer;

    public AdditionalRendererHolder(EntityRendererFactory.Context context, @NotNull AdditionalRendererState state) {
        this.context = context;
        this.state = state;
    }

    public void applyState() {
        if (state.isRendererEnabled())
            createRenderer();
    }

    private void createRenderer() {
        if (state.getPlayerModel() != null)
            this.additionalRenderer = new ReplacedPlayerEntityRenderer(context, state.getPlayerModel().getModelDataIdentifier());
    }

    @Nullable
    public ReplacedPlayerEntityRenderer getRenderer() {
        return this.additionalRenderer;
    }
}
