package com.codingcat.modelshifter.client.api.registry;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ModelRegistry {
    @NotNull
    private final static Set<PlayerModel> data;

    @Nullable
    public static <T extends PlayerModel> T register(@NotNull T model) {
        return data.add(model) ? model : null;
    }

    @NotNull
    public static Set<? extends PlayerModel> entries() {
        return data;
    }

    static {
        data = new HashSet<>();
    }
}
