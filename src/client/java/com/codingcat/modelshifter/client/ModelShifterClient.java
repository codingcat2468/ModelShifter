package com.codingcat.modelshifter.client;

import com.codingcat.modelshifter.client.api.renderer.DynamicAdditionalRendererHolder;
import com.codingcat.modelshifter.client.api.renderer.PlayerDependentStateHolder;
import com.codingcat.modelshifter.client.impl.Models;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class ModelShifterClient implements ClientModInitializer {
    public static final String MOD_ID = "modelshifter";
    public static final Identifier EMPTY_TEXTURE = Identifier.of(MOD_ID, "empty");
    public static PlayerDependentStateHolder state;
    public static DynamicAdditionalRendererHolder holder;

    @Override
    public void onInitializeClient() {
        Models.registerAll();
        this.loadConfig();
    }

    private void loadConfig() {
        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration config = loader.load(Configuration.class);

        ModeOption displayMode = ModeOption.byId(config.getDisplayMode());
        state = new PlayerDependentStateHolder(config.getGlobalState(), config.getPlayerOverrides(), displayMode != null ? displayMode : ModeOption.ALL);
        loader.write(config);
    }
}
