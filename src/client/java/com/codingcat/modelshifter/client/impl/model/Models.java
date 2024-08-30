package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;

public class Models {
    public static final PlayerModel CAT_PLAYER_MODEL = ModelRegistry.register(new CatPlayerModel());
    public static final PlayerModel AMONG_US_PLAYER_MODEL = ModelRegistry.register(new AmongUsPlayerModel());
    public static final PlayerModel WITHER_PLAYER_MODEL = ModelRegistry.register(new WitherPlayerModel());
}
