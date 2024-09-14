package com.codingcat.modelshifter.client.api.registry;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModelRegistry {
    @NotNull
    private final static HashMap<Pair<Integer, Identifier>, PlayerModel> data = new HashMap<>();

    public static void register(int ordinal, @NotNull Identifier id, @NotNull PlayerModel model) {
        data.put(Pair.of(ordinal, id), model);
    }

    @NotNull
    public static Optional<PlayerModel> get(@NotNull Identifier id) {
        return data.entrySet().stream()
                .filter(entry -> entry.getKey().getValue().equals(id))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @NotNull
    public static Optional<Identifier> findId(@NotNull PlayerModel model) {
        return data.entrySet().stream()
                .filter(entry -> entry.getValue().equals(model))
                .map(entry -> entry.getKey().getValue())
                .findFirst();
    }

    @NotNull
    public static List<Pair<Identifier, PlayerModel>> entriesSorted() {
        return data.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    int k1 = entry1.getKey().getKey();
                    int k2 = entry2.getKey().getKey();

                    return k1 - k2;
                })
                .map(entry -> Pair.of(entry.getKey().getValue(), entry.getValue()))
                .toList();
    }
}
