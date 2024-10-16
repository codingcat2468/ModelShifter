package com.codingcat.modelshifter.client.impl;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.impl.model.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Models {
    public static void registerAll() {
        register(0, "cat", new CatPlayerModel());
        register(1, "among_us", new AmongUsPlayerModel());
        register(2, "wither", new WitherPlayerModel());
        register(3, "chest", new ChestPlayerModel());
        register(4, "ghast", new GhastPlayerModel());
        register(5, "armadillo", new ArmadilloPlayerModel());
        register(6, "enderman", new EndermanPlayerModel());
        register(7, "flat_player", new FlatPlayerModel());
        register(8, "armor_stand", new ArmorStandPlayerModel());
        register(9, "frog", new FrogPlayerModel());
        register(10, "croissant", new CroissantPlayerModel());
        register(11, "square", new SquarePlayerModel());
        register(12, "dino", new DinoPlayerModel());
        register(13, "baby", new BabyPlayerModel());
        register(14, "creaking", new CreakingPlayerModel());
        register(15, "crab", new CrabPlayerModel());
    }

    private static void register(int ordinal, String id, PlayerModel playerModel) {
        ModelRegistry.register(ordinal, Identifier.of(ModelShifterClient.MOD_ID, id), playerModel);
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
