package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import org.jetbrains.annotations.Nullable;

public class AdditionalRendererState {
    private boolean rendererEnabled;
    @Nullable
    private PlayerModel playerModel;

    public AdditionalRendererState(boolean rendererEnabled, @Nullable PlayerModel playerModel) {
        this.rendererEnabled = rendererEnabled;
        this.playerModel = playerModel;
    }

    public boolean isRendererEnabled() {
        return rendererEnabled;
    }

    public void setRendererEnabled(boolean rendererEnabled) {
        this.rendererEnabled = rendererEnabled;
    }

    @Nullable
    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public DisabledFeatureRenderers accessDisabledFeatureRenderers() {
        if (getPlayerModel() == null) return null;
        return getPlayerModel().getDisabledFeatureRenderers();
    }

    public void setPlayerModel(@Nullable PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void setState(boolean rendererEnabled, @Nullable PlayerModel model) {
        setRendererEnabled(rendererEnabled);
        setPlayerModel(model);
    }
}
