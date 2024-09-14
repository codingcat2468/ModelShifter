package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PlayerDependentStateHolder {
    @NotNull
    private ModeOption displayMode;
    @NotNull
    private final AdditionalRendererState globalState;
    private final HashMap<UUID, AdditionalRendererState> stateOverrideMap;

    public PlayerDependentStateHolder(boolean globalRendererEnabled, @Nullable PlayerModel globalModel, @NotNull ModeOption displayMode) {
        this.stateOverrideMap = new HashMap<>();
        this.globalState = new AdditionalRendererState(globalRendererEnabled, globalModel);
        this.displayMode = displayMode;
    }

    public void setDisplayMode(@NotNull ModeOption displayMode) {
        this.displayMode = displayMode;
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

    public FeatureRendererStates accessDisabledFeatureRenderers(PlayerEntity entity) {
        return getState(entity.getUuid()).accessFeatureRendererStates();
    }

    public boolean isRendererStateEnabled(UUID uuid) {
        return getState(uuid).isRendererEnabled();
    }

    public boolean isRendererEnabled(UUID uuid) {
        if (displayMode == ModeOption.ONLY_ME && !isSelf(uuid)) return false;
        if (displayMode == ModeOption.ONLY_OTHERS && isSelf(uuid)) return false;

        return isRendererStateEnabled(uuid);
    }

    public boolean isRendererEnabled(PlayerEntity entity) {
        return isRendererEnabled(entity.getUuid());
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

    private boolean isSelf(UUID uuid) {
        GameProfile profile = MinecraftClient.getInstance().getGameProfile();
        return uuid.equals(profile.getId());
    }

    public AdditionalRendererState getState(UUID uuid) {
        if (hasUniqueState(uuid))
            return stateOverrideMap.get(uuid);

        return this.globalState;
    }
}
