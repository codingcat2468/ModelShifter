package com.codingcat.modelshifter.client.commands;

import com.codingcat.modelshifter.client.api.commands.ModCommand;
import com.codingcat.modelshifter.client.api.commands.ModCommandRegistry;
import com.codingcat.modelshifter.client.commands.dev.ReloadModelRegistryCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Set;

public class ClientCommands implements ModCommandRegistry<FabricClientCommandSource> {
    @Override
    public Set<ModCommand<FabricClientCommandSource>> getCommands() {
        return Set.of(new ModelCommand(), new ReloadModelRegistryCommand());
    }
}
