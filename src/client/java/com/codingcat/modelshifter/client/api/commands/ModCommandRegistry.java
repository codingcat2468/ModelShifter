package com.codingcat.modelshifter.client.api.commands;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

import java.util.Set;

public interface ModCommandRegistry<T extends CommandSource> {
    Set<ModCommand<T>> getCommands();

    default void registerAll(CommandDispatcher<T> dispatcher) {
        getCommands().forEach(command -> {
            if (command.isDevOnly() && !ModelShifterClient.isDev) return;
            command.register(dispatcher);
        });
    }
}
