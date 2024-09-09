package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class AdditionalRendererHolder {
    private final EntityRendererFactory.Context context;
    @NotNull
    private final PlayerDependentStateHolder stateHolder;
    @NotNull
    private final Set<ReplacedPlayerEntityRenderer> additionalRendererSet;

    public AdditionalRendererHolder(EntityRendererFactory.Context context, @NotNull PlayerDependentStateHolder stateHolder) {
        this.context = context;
        this.stateHolder = stateHolder;
        this.additionalRendererSet = new HashSet<>();
    }

    public void applyState() {
        AdditionalRendererState globalState = stateHolder.getGlobalState();
        if (!globalState.isRendererEnabled()) {
            writeConfig(globalState);
            return;
        }

        this.additionalRendererSet.clear();
        PlayerModel globalModel = globalState.getPlayerModel();
        if (globalModel != null)
            this.additionalRendererSet.add(createRendererInstance(globalModel.getModelDataIdentifier()));

        for (UUID uuid : stateHolder.getStoredPlayers()) {
            if (!stateHolder.hasUniqueState(uuid)) continue;

            AdditionalRendererState state = stateHolder.getState(uuid);
            if (state.isRendererEnabled() && state.getPlayerModel() != null)
                tryAddRenderer(state.getPlayerModel());
        }

        writeConfig(globalState);
    }

    private void writeConfig(AdditionalRendererState globalState) {
        Optional<Identifier> modelId = globalState.getPlayerModel() != null ? ModelRegistry.findId(globalState.getPlayerModel()) : Optional.empty();
        new ConfigurationLoader().write(new Configuration()
                .setRendererEnabled(globalState.isRendererEnabled())
                .setModelIdentifier(modelId.map(Identifier::toString).orElse(null)));
    }

    public ReplacedPlayerEntityRenderer createRendererInstance(Identifier modelIdentifier) {
        return new ReplacedPlayerEntityRenderer(context, modelIdentifier);
    }

    private Predicate<ReplacedPlayerEntityRenderer> findRenderer(@NotNull PlayerModel model) {
        return renderer -> renderer.getModelIdentifier().equals(model.getModelDataIdentifier());
    }

    private void tryAddRenderer(@NotNull PlayerModel model) {
        boolean rendererExists = additionalRendererSet.stream().anyMatch(findRenderer(model));
        if (!rendererExists)
            this.additionalRendererSet.add(createRendererInstance(model.getModelDataIdentifier()));
    }

    @Nullable
    public ReplacedPlayerEntityRenderer getRenderer(PlayerModel model) {
        Optional<ReplacedPlayerEntityRenderer> renderer = additionalRendererSet.stream().filter(findRenderer(model)).findFirst();
        return renderer.orElse(null);
    }
}
