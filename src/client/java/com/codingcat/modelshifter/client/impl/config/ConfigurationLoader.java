package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.JsonConfigurationElement;
import com.codingcat.modelshifter.client.api.config.JsonConfigurationLoader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

                if (field.getType() == int.class)
                    field.set(config, element.getAsInt());
                else if (field.getType() == boolean.class)
                    field.set(config, element.getAsBoolean());
                else if (field.getType() == String.class)
                    field.set(config, element.getAsString());
            }

            return config;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
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

                if (field.getType() == int.class)
                    object.addProperty(name, field.getInt(instance));
                else if (field.getType() == boolean.class)
                    object.addProperty(name, field.getBoolean(instance));
                else if (field.getType() == String.class)
                    object.addProperty(name, (String) field.get(instance));
            }

            String json = gson.toJson(object, JsonObject.class);
            FileUtils.writeStringToFile(instance.getConfigFile(), json, "UTF-8");
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException("Failed to write configuration", e);
        }
    }
}
