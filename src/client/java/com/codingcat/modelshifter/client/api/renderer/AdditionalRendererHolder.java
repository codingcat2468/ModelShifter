package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

        Optional<Identifier> modelId = state.getPlayerModel() != null ? ModelRegistry.findId(state.getPlayerModel()) : Optional.empty();
        new ConfigurationLoader().write(new Configuration()
                .setRendererEnabled(state.isRendererEnabled())
                .setModelIdentifier(modelId.map(Identifier::toString).orElse(null)));
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
