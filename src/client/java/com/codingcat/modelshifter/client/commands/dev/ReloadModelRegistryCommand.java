package com.codingcat.modelshifter.client.commands.dev;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.commands.ModCommand;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.impl.Models;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ReloadModelRegistryCommand implements ModCommand<FabricClientCommandSource> {
    private static final Text RELOAD_SUCCESS = Text.translatable("modelshifter.commmand.dev.reloadmodelregistry");

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("reloadmodelregistry")
                .executes(this::execute));
    }

    @SuppressWarnings({"DataFlowIssue", "OptionalGetWithoutIsPresent"})
    public int execute(CommandContext<FabricClientCommandSource> context) {
        try {
            ModelRegistry.clear();
            Models.registerAll();
            AdditionalRendererState state = ModelShifterClient.state.getGlobalState();
            Identifier id = ModelRegistry.findId(state.getPlayerModel()).orElse(null);
            ModelShifterClient.state.setGlobalState(state.isRendererEnabled(), ModelRegistry.get(id).get());
            context.getSource().sendFeedback(RELOAD_SUCCESS);

            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
