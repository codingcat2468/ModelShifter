package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.codingcat.modelshifter.client.render.ReplacedPlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class DynamicAdditionalRendererHolder {
    private final EntityRendererFactory.Context context;
    @NotNull
    private final PlayerDependentStateHolder stateHolder;
    @NotNull
    private final Set<ReplacedPlayerEntityRenderer> additionalRendererSet;

    public DynamicAdditionalRendererHolder(EntityRendererFactory.Context context, @NotNull PlayerDependentStateHolder stateHolder) {
        this.context = context;
        this.stateHolder = stateHolder;
        this.additionalRendererSet = new HashSet<>();
    }

    public void applyState() {
        AdditionalRendererState globalState = stateHolder.getGlobalState();
        this.additionalRendererSet.clear();
        if (!globalState.isRendererEnabled()) {
            writeConfig();
            return;
        }

        PlayerModel globalModel = globalState.getPlayerModel();
        if (globalState.isRendererEnabled() && globalModel != null)
            this.additionalRendererSet.add(createRendererInstance(globalModel));

        for (UUID uuid : stateHolder.getStoredPlayers()) {
            if (!stateHolder.hasUniqueState(uuid)) continue;

            AdditionalRendererState state = stateHolder.getState(uuid);
            if (state.isRendererEnabled() && state.getPlayerModel() != null)
                tryAddRenderer(state.getPlayerModel());
        }

        writeConfig();
    }

    private void writeConfig() {
        new ConfigurationLoader().write(new Configuration()
                .setGlobalState(stateHolder.getGlobalState())
                .setPlayerOverrides(stateHolder.generateOverrides())
                .setDisplayMode(stateHolder.getDisplayMode().getId()));
    }

    public ReplacedPlayerEntityRenderer createRendererInstance(PlayerModel model) {
        return new ReplacedPlayerEntityRenderer(context, model);
    }

    private Predicate<ReplacedPlayerEntityRenderer> findRenderer(@NotNull PlayerModel model) {
        return renderer -> renderer.getModelIdentifier().equals(model.getModelDataIdentifier());
    }

    private void tryAddRenderer(@NotNull PlayerModel model) {
        boolean rendererExists = additionalRendererSet.stream().anyMatch(findRenderer(model));
        if (!rendererExists)
            this.additionalRendererSet.add(createRendererInstance(model));
    }

    @Nullable
    public ReplacedPlayerEntityRenderer getRenderer(PlayerModel model) {
        Optional<ReplacedPlayerEntityRenderer> renderer = additionalRendererSet.stream().filter(findRenderer(model)).findFirst();
        return renderer.orElse(null);
    }

    public int getRendererCount() {
        return this.additionalRendererSet.size();
    }
}
