package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
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

    public ReplacedPlayerEntityRenderer createRendererInstance(Identifier modelIdentifier) {
        return new ReplacedPlayerEntityRenderer(context, modelIdentifier);
    }

    private void createRenderer() {
        if (state.getPlayerModel() != null)
            this.additionalRenderer = createRendererInstance(state.getPlayerModel().getModelDataIdentifier());
    }

    @Nullable
    public ReplacedPlayerEntityRenderer getRenderer() {
        return this.additionalRenderer;
    }
}
