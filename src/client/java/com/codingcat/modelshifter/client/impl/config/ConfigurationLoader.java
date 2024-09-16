package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.JsonConfigurationElement;
import com.codingcat.modelshifter.client.api.config.JsonConfigurationLoader;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationLoader implements JsonConfigurationLoader<Configuration> {

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

    private static JsonElement serialize(Type type, Object data) {
        Gson gson = new Gson();
        Object object = null;
        if (type == ArrayList.class)
            object = serializeArrayList((ArrayList<?>) data);
        else if (type == AdditionalRendererState.class)
            object = ((AdditionalRendererState) data).serialize();
        else if (type == ConfigPlayerOverride.class)
            object = ((ConfigPlayerOverride) data).serialize();

        return object != null ? gson.toJsonTree(object) : gson.toJsonTree(data);
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
            array.add(serialize(element.getClass(), element));
        });
        return array;
    }

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
        } else if (type == AdditionalRendererState.class) {
            try {
                return AdditionalRendererState.deserialize(element.getAsJsonObject());
            } catch (IOException e) {
                return new AdditionalRendererState();
            }
        } else if (type == ConfigPlayerOverride.class) {
            try {
                return ConfigPlayerOverride.deserialize(element.getAsJsonObject());
            } catch (IOException e) {
                return new ConfigPlayerOverride(null, null);
            }
        }

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
