package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.UUID;

public record ConfigPlayerOverride(
        UUID player,
        AdditionalRendererState state
) {
    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("player", player().toString());
        object.add("state", state.serialize());

        return object;
    }

    public static ConfigPlayerOverride deserialize(JsonObject object) throws IOException, IllegalArgumentException {
        String uuid = object.get("player").getAsString();
        AdditionalRendererState state1 = AdditionalRendererState.deserialize(object.getAsJsonObject("state"));

        return new ConfigPlayerOverride(UUID.fromString(uuid), state1);
    }
}
