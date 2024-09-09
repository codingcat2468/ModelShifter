package com.codingcat.modelshifter.client;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererHolder;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.impl.Models;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelShifterClient implements ClientModInitializer {
    public static final String MOD_ID = "modelshifter";
    public static final Identifier EMPTY_TEXTURE = Identifier.of(MOD_ID, "empty");
    public static AdditionalRendererState state;
    public static AdditionalRendererHolder holder;

    @Override
    public void onInitializeClient() {
        Models.registerAll();
        this.loadConfig();
    }

    private void loadConfig() {
        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration config = loader.load(Configuration.class);
        Identifier identifier = config.getModelIdentifier() != null ? Identifier.tryParse(config.getModelIdentifier()) : null;
        Optional<PlayerModel> model = identifier != null ? ModelRegistry.get(identifier) : Optional.empty();
        if (model.isEmpty())
            config.setRendererEnabled(false);

        state = new AdditionalRendererState(config.isRendererEnabled(), model.orElse(null));
        loader.write(config);
    }
}
