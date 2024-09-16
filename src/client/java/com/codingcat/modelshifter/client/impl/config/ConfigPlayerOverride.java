package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializable;
import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializer;
import com.codingcat.modelshifter.client.api.config.serialize.JsonSerializerFactory;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.UUID;

public record ConfigPlayerOverride(
        UUID player,
        AdditionalRendererState state
) implements JsonConfigSerializable<ConfigPlayerOverride.Serializer> {

    @JsonSerializerFactory
    public static ConfigPlayerOverride.Serializer createSerializer() {
        return new ConfigPlayerOverride.Serializer();
    }

    public static class Serializer implements JsonConfigSerializer<ConfigPlayerOverride, JsonObject> {

        @Override
        public JsonObject serialize(ConfigPlayerOverride override) {
            JsonObject object = new JsonObject();
            object.addProperty("player", override.player().toString());
            object.add("state", AdditionalRendererState.createSerializer().serialize(override.state()));

            return object;
        }

        @Override
        public ConfigPlayerOverride deserialize(JsonObject object) throws IOException {
            try {
                String uuid = object.get("player").getAsString();
                AdditionalRendererState state1 = AdditionalRendererState.createSerializer().deserialize(object.getAsJsonObject("state"));

                return new ConfigPlayerOverride(UUID.fromString(uuid), state1);
            } catch (IllegalArgumentException e) {
                throw new IOException(e);
            }
        }
    }
}
