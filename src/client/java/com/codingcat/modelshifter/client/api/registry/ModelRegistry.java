package com.codingcat.modelshifter.client.api.registry;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ModelRegistry {
    @NotNull
    private final static HashMap<Identifier, PlayerModel> data = new HashMap<>();

    public static void register(@NotNull Identifier id, @NotNull PlayerModel model) {
        data.put(id, model);
    }

    @NotNull
    public static Optional<PlayerModel> get(@NotNull Identifier id) {
        PlayerModel model = data.get(id);
        return model != null ? Optional.of(model) : Optional.empty();
    }

    @NotNull
    public static Optional<Identifier> findId(@NotNull PlayerModel model) {
        return data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(model))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    @NotNull
    public static Set<Map.Entry<Identifier, PlayerModel>> entries() {
        return data.entrySet();
    }
}
