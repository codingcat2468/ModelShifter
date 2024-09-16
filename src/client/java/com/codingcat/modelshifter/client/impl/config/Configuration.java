package com.codingcat.modelshifter.client.impl.config;

import com.codingcat.modelshifter.client.api.config.JsonConfiguration;
import com.codingcat.modelshifter.client.api.config.JsonConfigurationElement;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

public class Configuration implements JsonConfiguration {
    private final static String FILE_NAME = "modelshifter.json";

    @JsonConfigurationElement(propertyName = "global_state")
    private AdditionalRendererState globalState = new AdditionalRendererState(false, null);

    @JsonConfigurationElement(propertyName = "display_mode")
    private String displayMode = ModeOption.ALL.getId();

    @JsonConfigurationElement(propertyName = "player_overrides")
    private ArrayList<ConfigPlayerOverride> playerOverrides = new ArrayList<>();

    @Override
    public File getConfigFile() throws IOException {
        Path filePath = Path.of(FabricLoader.getInstance().getConfigDir().toString(), FILE_NAME);
        File file = filePath.toFile();
        if (!file.exists())
            FileUtils.writeStringToFile(file, "{}", "UTF-8");

        return file;
    }

    public AdditionalRendererState getGlobalState() {
        return this.globalState;
    }

    public Configuration setGlobalState(AdditionalRendererState globalState) {
        this.globalState = globalState;
        return this;
    }

    public ArrayList<ConfigPlayerOverride> getPlayerOverrides() {
        return playerOverrides;
    }

    public Configuration setPlayerOverrides(Set<ConfigPlayerOverride> playerOverrides) {
        this.playerOverrides = new ArrayList<>(playerOverrides);
        return this;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public Configuration setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
        return this;
    }
}
