package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.impl.config.ConfigPlayerOverride;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerDependentStateHolder {
    @NotNull
    private ModeOption displayMode;
    @NotNull
    private final AdditionalRendererState globalState;
    private HashMap<UUID, AdditionalRendererState> stateOverrideMap;

    public PlayerDependentStateHolder(@NotNull AdditionalRendererState globalState, Set<ConfigPlayerOverride> overrides, @NotNull ModeOption displayMode) {
        this.reloadFromOverrides(overrides);
        this.globalState = globalState;
        this.displayMode = displayMode;
    }

    public void reloadFromOverrides(Set<ConfigPlayerOverride> overrides) {
        this.stateOverrideMap = new HashMap<>(createFromOverrides(overrides));
    }

    private Map<UUID, AdditionalRendererState> createFromOverrides(Set<ConfigPlayerOverride> overrides) {
        return overrides.stream()
                .collect(Collectors.toMap(ConfigPlayerOverride::player, ConfigPlayerOverride::state));
    }

    public Set<ConfigPlayerOverride> generateOverrides() {
        return stateOverrideMap.entrySet()
                .stream()
                .map(entry -> new ConfigPlayerOverride(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    public void setDisplayMode(@NotNull ModeOption displayMode) {
        this.displayMode = displayMode;
    }

    public @NotNull ModeOption getDisplayMode() {
        return displayMode;
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

    public FeatureRendererStates accessFeatureRendererStates(PlayerEntity entity) {
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

    public int getStateCount() {
        return stateOverrideMap.size();
    }

    public int getActiveStateCount() {
        return (int) stateOverrideMap.values()
                .stream()
                .filter(AdditionalRendererState::isRendererEnabled)
                .count();
    }
}
