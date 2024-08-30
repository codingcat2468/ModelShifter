package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import net.minecraft.util.Identifier;

public class Models {
    public static final PlayerModel CAT_PLAYER_MODEL = register("cat", new CatPlayerModel());
    public static final PlayerModel AMONG_US_PLAYER_MODEL = register("among_us", new AmongUsPlayerModel());
    public static final PlayerModel WITHER_PLAYER_MODEL = register("wither", new WitherPlayerModel());

    private static PlayerModel register(String id, PlayerModel playerModel) {
        return ModelRegistry.register(new Identifier(ModelShifterClient.MOD_ID, id), playerModel);
    }
}
