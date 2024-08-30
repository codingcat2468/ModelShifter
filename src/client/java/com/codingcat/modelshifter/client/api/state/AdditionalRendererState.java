package com.codingcat.modelshifter.client.api.state;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public record AdditionalRendererState(
        AtomicBoolean rendererEnabled,
        AtomicReference<PlayerModel> model,
        AtomicReference<Consumer<PlayerModel>> onRecreateRenderer
) {

    public void setState(boolean enableRenderer, @Nullable PlayerModel model) {
        this.model.set(model);
        if (enableRenderer)
            this.callRecreateAdditionalRenderer();
        this.rendererEnabled.set(enableRenderer);
    }

    public void setRecreateRendererCallback(Consumer<PlayerModel> onRecreateRenderer) {
        this.onRecreateRenderer.set(onRecreateRenderer);
    }

    public void callRecreateAdditionalRenderer() {
        if (this.onRecreateRenderer.get() != null)
            this.onRecreateRenderer.get().accept(this.model.get());
    }

    public DisabledFeatureRenderers getDisabledFeatureRenderers() {
        return this.model.get().getDisabledFeatureRenderers();
    }
}
