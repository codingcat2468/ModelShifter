package com.codingcat.modelshifter.client.api.registry;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelRegistry {
    @NotNull
    private final static HashMap<Identifier, PlayerModel> data;

    @Nullable
    public static PlayerModel register(Identifier id, @NotNull PlayerModel model) {
        return data.put(id, model);
    }

    @Nullable
    public static PlayerModel get(Identifier id) {
        return data.get(id);
    }

    @NotNull
    public static Set<Map.Entry<Identifier, PlayerModel>> entries() {
        return data.entrySet();
    }

    static {
        data = new HashMap<>();
    }
}
