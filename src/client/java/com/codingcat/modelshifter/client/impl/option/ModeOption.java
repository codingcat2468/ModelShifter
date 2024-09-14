package com.codingcat.modelshifter.client.impl.option;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public enum ModeOption {
    ALL("all_players"),
    ONLY_ME("only_self"),
    ONLY_OTHERS("only_other_players");

    public static final List<ModeOption> OPTIONS = Arrays.stream(values()).toList();
    private final Text displayName;
    @NotNull
    private final String id;

    ModeOption(@NotNull String id) {
        this.displayName = Text.translatable(String.format("modelshifter.option.mode.%s", id));
        this.id = id;
    }

    @Nullable
    public static ModeOption byId(@NotNull String id) {
        return Arrays.stream(values())
                .filter(modeOption -> modeOption.getId().equals(id))
                .findFirst().orElse(null);
    }

    public Text getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getId() {
        return id;
    }
}
