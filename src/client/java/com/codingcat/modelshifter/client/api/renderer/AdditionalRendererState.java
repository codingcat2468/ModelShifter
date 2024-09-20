package com.codingcat.modelshifter.client.api.renderer;

import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializable;
import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializer;
import com.codingcat.modelshifter.client.api.config.serialize.JsonSerializerFactory;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

public class AdditionalRendererState implements JsonConfigSerializable<AdditionalRendererState.Serializer> {
    private boolean rendererEnabled;
    @Nullable
    private PlayerModel playerModel;

    public AdditionalRendererState() {
        this.rendererEnabled = false;
        this.playerModel = null;
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
        return getPlayerModel().getFeatureRendererStates();
    }

    public void setPlayerModel(@Nullable PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void setState(boolean rendererEnabled, @Nullable PlayerModel model) {
        setRendererEnabled(rendererEnabled);
        setPlayerModel(model);
    }

    @JsonSerializerFactory
    public static AdditionalRendererState.Serializer createSerializer() {
        return new AdditionalRendererState.Serializer();
    }

    public static class Serializer implements JsonConfigSerializer<AdditionalRendererState, JsonObject> {

        @Override
        public JsonObject serialize(AdditionalRendererState state) {
            JsonObject object = new JsonObject();
            Optional<Identifier> modelId = state.getPlayerModel() != null ? ModelRegistry.findId(state.getPlayerModel()) : Optional.empty();
            object.addProperty("renderer_enabled", state.isRendererEnabled());
            modelId.ifPresent(identifier ->
                    object.addProperty("player_model", identifier.toString()));

            return object;
        }

        @Override
        public AdditionalRendererState deserialize(JsonObject object) throws IOException {
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
}
