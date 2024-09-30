package com.codingcat.modelshifter.client.commands.dev;

import com.codingcat.modelshifter.client.api.commands.ModCommand;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.impl.Models;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class ReloadModelRegistryCommand implements ModCommand<FabricClientCommandSource> {
    private static final Text RELOAD_SUCCESS = Text.translatable("modelshifter.commmand.dev.reloadmodelregistry");

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("reloadmodelregistry")
                .executes(this::execute));
    }

    public int execute(CommandContext<FabricClientCommandSource> context) {
        ModelRegistry.clear();
        Models.registerAll();
        context.getSource().sendFeedback(RELOAD_SUCCESS);

        return 1;
    }
}
