package com.codingcat.modelshifter.client.api.state;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public record AdditionalRendererState(
        AtomicBoolean rendererEnabled,
        AtomicReference<PlayerModel> model,
        Consumer<PlayerModel> onRecreateRenderer
) {
    public void setState(boolean enableRenderer, @Nullable PlayerModel model) {
        if (this.onRecreateRenderer != null && enableRenderer)
            this.onRecreateRenderer.accept(model);
        this.rendererEnabled.set(enableRenderer);
        this.model.set(model);
    }

    public DisabledFeatureRenderers getDisabledFeatureRenderers() {
        return this.model.get().getDisabledFeatureRenderers();
    }
}
