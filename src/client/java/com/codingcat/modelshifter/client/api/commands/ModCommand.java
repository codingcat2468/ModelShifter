package com.codingcat.modelshifter.client.api.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public interface ModCommand<T extends CommandSource> {
    void register(CommandDispatcher<T> dispatcher);

    default boolean isDevOnly() {
        return false;
    }
}
