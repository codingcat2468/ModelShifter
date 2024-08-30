package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Models {

    public static void registerAll() {
        register("cat", new CatPlayerModel());
        register("among_us", new AmongUsPlayerModel());
        register("wither", new WitherPlayerModel());
    }

    private static void register(String id, PlayerModel playerModel) {
        ModelRegistry.register(new Identifier(ModelShifterClient.MOD_ID, id), playerModel);
    }

    @Nullable
    public static String getTranslationKey(PlayerModel model) {
        Optional<Identifier> id = ModelRegistry.findId(model);
        return id.map(Models::getTranslationKey).orElse(null);
    }

    @NotNull
    public static String getTranslationKey(Identifier id) {
        return String.format("modelshifter.model.%s", id.getPath());
    }
}
