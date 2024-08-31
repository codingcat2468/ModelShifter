package com.codingcat.modelshifter.client.api.config;

public interface JsonConfigurationLoader<T extends JsonConfiguration> {
    T load(Class<T> cls);
    void write(T instance);
}
