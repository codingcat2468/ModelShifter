package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.JsonConfiguration;
import com.codingcat.modelshifter.client.api.config.JsonConfigurationElement;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Configuration implements JsonConfiguration {
    private final static String FILE_NAME = "modelshifter.json";

    @JsonConfigurationElement(propertyName = "renderer_enabled")
    private boolean rendererEnabled = false;

    @JsonConfigurationElement(propertyName = "model_identifier")
    private String modelIdentifier = null;

    @Override
    public File getConfigFile() throws IOException {
        Path filePath = Path.of(FabricLoader.getInstance().getConfigDir().toString(), FILE_NAME);
        File file = filePath.toFile();
        if (!file.exists())
            FileUtils.writeStringToFile(file, "{}", "UTF-8");

        return file;
    }

    public boolean isRendererEnabled() {
        return rendererEnabled;
    }

    public Configuration setRendererEnabled(boolean rendererEnabled) {
        this.rendererEnabled = rendererEnabled;
        return this;
    }

    public String getModelIdentifier() {
        return modelIdentifier;
    }

    public Configuration setModelIdentifier(String modelIdentifier) {
        this.modelIdentifier = modelIdentifier;
        return this;
    }
}
