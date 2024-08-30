package com.codingcat.modelshifter.client;

import com.codingcat.modelshifter.client.impl.model.Models;
import net.fabricmc.api.ClientModInitializer;

public class ModelShifterClient implements ClientModInitializer {
    public static final String MOD_ID = "modelshifter";
    public static AdditionalRendererState state;
    public static AdditionalRendererHolder holder;

    @Override
    public void onInitializeClient() {
        Models.registerAll();
    }
}
