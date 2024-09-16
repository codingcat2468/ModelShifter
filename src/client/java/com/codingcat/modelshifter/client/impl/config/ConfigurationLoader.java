package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.JsonConfigurationElement;
import com.codingcat.modelshifter.client.api.config.JsonConfigurationLoader;
import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializable;
import com.codingcat.modelshifter.client.api.config.serialize.JsonConfigSerializer;
import com.codingcat.modelshifter.client.api.config.serialize.JsonSerializerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConfigurationLoader implements JsonConfigurationLoader<Configuration> {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public Configuration load(Class<Configuration> cls) {
        try {
            Configuration config = cls.getConstructor().newInstance();
            String string = FileUtils.readFileToString(config.getConfigFile(), "UTF-8");

            Gson gson = new Gson();
            JsonObject object = gson.fromJson(string, JsonObject.class);
            for (Field field : config.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(JsonConfigurationElement.class)) continue;
                JsonConfigurationElement annotation = field.getAnnotation(JsonConfigurationElement.class);
                JsonElement element = object.get(annotation.propertyName());
                field.setAccessible(true);
                if (element == null) continue;

                Object data = deserialize(field.getType(), field.getGenericType(), element);
                field.set(config, data);
                field.setAccessible(false);
            }

            return config;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static JsonElement serialize(Type type, Object data) throws IOException {
        Gson gson = new Gson();
        Object object = null;
        if (type == ArrayList.class)
            object = serializeArrayList((ArrayList<?>) data);
        if (data instanceof JsonConfigSerializable<?> serializable) {
            Optional<JsonConfigSerializer<JsonConfigSerializable<?>, JsonElement>> result = tryGetSerializer((Class<JsonConfigSerializable<?>>) serializable.getClass());
            if (result.isPresent())
                return result.get().serialize(serializable);
        }

        return object != null ? gson.toJsonTree(object) : gson.toJsonTree(data);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private static <E extends JsonElement, T extends JsonConfigSerializable<?>> Optional<JsonConfigSerializer<T, E>> tryGetSerializer(Class<T> serializable) {
        try {
            Optional<Method> serializerFactory = Arrays.stream(serializable.getMethods())
                    .filter(method -> method.isAnnotationPresent(JsonSerializerFactory.class))
                    .findFirst();

            if (serializerFactory.isEmpty())
                return Optional.empty();

            Object result = serializerFactory.get().invoke(null);
            return Optional.of((JsonConfigSerializer<T, E>) result);
        } catch (ReflectiveOperationException | RuntimeException e) {
            return Optional.empty();
        }
    }

    private static ArrayList<Object> deserializeArrayList(Type listType, JsonArray array) {
        if (!(listType instanceof ParameterizedType type)) throw new IllegalArgumentException();
        Type elementType = type.getActualTypeArguments()[0];

        List<Object> list = array.asList()
                .stream()
                .map(element -> deserialize(elementType, null, element))
                .toList();
        return new ArrayList<>(list);
    }

    private static JsonArray serializeArrayList(ArrayList<?> arrayList) {
        JsonArray array = new JsonArray();
        arrayList.forEach(element -> {
            if (element == null) return;

            try {
                array.add(serialize(element.getClass(), element));
            } catch (IOException ignored) {
                LOGGER.error("Failed to serialize config element in list: {}", element);
            }
        });
        return array;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static Object deserialize(Type type, Type genericType, JsonElement element) {
        if (type == int.class)
            return element.getAsInt();
        else if (type == boolean.class)
            return element.getAsBoolean();
        else if (type == String.class)
            return element.getAsString();
        else if (type == ArrayList.class) {
            return deserializeArrayList(genericType, element.getAsJsonArray());
        } else if (Arrays.stream(((Class<?>) type).getGenericInterfaces())
                .anyMatch(i -> {
                    if (!(i instanceof ParameterizedType type1)) return false;
                    return type1.getRawType() == JsonConfigSerializable.class;
                })) {
            Optional<JsonConfigSerializer<JsonConfigSerializable<?>, JsonElement>> result = tryGetSerializer((Class<JsonConfigSerializable<?>>) type);
            if (result.isPresent()) {
                try {
                    return result.get().deserialize(element);
                } catch (IOException e) {
                    LOGGER.error("Failed to deserialize part of the config", e);
                }
            }
        }

        LOGGER.error("Failed to deserialize config element {}", element.toString());
        return null;
    }

    @Override
    public void write(Configuration instance) {
        try {
            Gson gson = new Gson();
            JsonObject object = new JsonObject();
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(JsonConfigurationElement.class)) continue;
                JsonConfigurationElement annotation = field.getAnnotation(JsonConfigurationElement.class);
                String name = annotation.propertyName();
                field.setAccessible(true);
                if (field.get(instance) == null) {
                    object.add(name, null);
                    continue;
                }

                object.add(name, serialize(field.getType(), field.get(instance)));
            }

            String json = gson.toJson(object, JsonObject.class);
            FileUtils.writeStringToFile(instance.getConfigFile(), json, "UTF-8");
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException("Failed to write configuration", e);
        }
    }
}
