package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

public class AdditionalRendererState {
    private boolean rendererEnabled;
    @Nullable
    private PlayerModel playerModel;

    public AdditionalRendererState() {
    }

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

    public FeatureRendererStates accessFeatureRendererStates() {
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

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        Optional<Identifier> modelId = getPlayerModel() != null ? ModelRegistry.findId(getPlayerModel()) : Optional.empty();
        object.addProperty("renderer_enabled", isRendererEnabled());
        modelId.ifPresent(identifier ->
                object.addProperty("player_model", identifier.toString()));

        return object;
    }

    public static AdditionalRendererState deserialize(JsonObject object) throws IOException {
        try {
            boolean rendererEnabled = object.get("renderer_enabled").getAsBoolean();
            Identifier modelId = null;
            if (object.has("player_model"))
                modelId = Identifier.tryParse(object.get("player_model").getAsString());
            Optional<PlayerModel> model = modelId != null ? ModelRegistry.get(modelId) : Optional.empty();

            return new AdditionalRendererState(rendererEnabled, model.orElse(null));
        } catch (UnsupportedOperationException | IllegalStateException | NullPointerException e) {
            throw new IOException("Failed to serialize AdditionalRenderState from config", e);
        }
    }
}
