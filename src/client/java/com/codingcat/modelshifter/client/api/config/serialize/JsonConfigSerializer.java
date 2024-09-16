package com.codingcat.modelshifter.client.api.config.serialize;

import com.google.gson.JsonElement;

import java.io.IOException;

public interface JsonConfigSerializer<T extends JsonConfigSerializable<?>, E extends JsonElement> {
    E serialize(T object) throws IOException;

    T deserialize(E jsonElement) throws IOException;
}
