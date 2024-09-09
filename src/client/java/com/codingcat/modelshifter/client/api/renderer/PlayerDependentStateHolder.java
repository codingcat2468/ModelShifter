package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PlayerDependentStateHolder {
    @NotNull
    private final AdditionalRendererState globalState;
    private final HashMap<UUID, AdditionalRendererState> stateOverrideMap;

    public PlayerDependentStateHolder(boolean globalRendererEnabled, PlayerModel globalModel) {
        this.stateOverrideMap = new HashMap<>();
        this.globalState = new AdditionalRendererState(globalRendererEnabled, globalModel);
    }

    public void setGlobalState(boolean rendererEnabled, @Nullable PlayerModel model) {
        this.globalState.setState(rendererEnabled, model);
    }

    public void setPlayerState(UUID uuid, boolean rendererEnabled, @Nullable PlayerModel model) {
        AdditionalRendererState state = stateOverrideMap.get(uuid);
        if (hasUniqueState(uuid))
            state.setState(rendererEnabled, model);
        this.stateOverrideMap.put(uuid, hasUniqueState(uuid) ? state : new AdditionalRendererState(rendererEnabled, model));
    }

    public void removePlayerState(UUID uuid) {
        this.stateOverrideMap.remove(uuid);
    }

    public DisabledFeatureRenderers accessDisabledFeatureRenderers(PlayerEntity entity) {
        return getState(entity.getUuid()).accessDisabledFeatureRenderers();
    }

    public boolean isRendererEnabled(UUID uuid) {
        return getState(uuid).isRendererEnabled();
    }

    public boolean isRendererEnabled(PlayerEntity entity) {
        return getState(entity.getUuid()).isRendererEnabled();
    }

    public Set<UUID> getStoredPlayers() {
        return this.stateOverrideMap.keySet();
    }

    @NotNull
    public AdditionalRendererState getGlobalState() {
        return this.globalState;
    }

    public boolean hasUniqueState(UUID uuid) {
        return this.stateOverrideMap.get(uuid) != null;
    }

    public AdditionalRendererState getState(UUID uuid) {
        if (!this.globalState.isRendererEnabled())
            return this.globalState;
        if (hasUniqueState(uuid))
            return stateOverrideMap.get(uuid);

        return this.globalState;
    }
}
