package com.codingcat.modelshifter.client.commands;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.commands.ModCommand;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.commands.argument.ModelIdentifierArgumentType;
import com.codingcat.modelshifter.client.impl.Models;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.function.Function;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ModelCommand implements ModCommand<FabricClientCommandSource> {
    private static final Function<Text, Text> SUCCESS_MODEL_CHANGED = model -> Text.translatable("modelshifter.commmand.model.success", model);
    private static final Text SUCCESS_MODEL_DISABLED = Text.translatable("modelshifter.commmand.model.success.disabled");

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("model")
                .then(argument("model", ModelIdentifierArgumentType.modelIdentifier())
                        .executes(this::execute)));
    }

    public int execute(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        PlayerModel model = ModelIdentifierArgumentType.getModel(context, "model");
        if (model != null)
            ModelShifterClient.state.setGlobalState(true, model);
        else
            ModelShifterClient.state.setGlobalState(false, null);

        ModelShifterClient.holder.applyState();
        Text modelName = Text.translatable(Models.getTranslationKey(model));
        context.getSource().sendFeedback(model != null ? SUCCESS_MODEL_CHANGED.apply(modelName) : SUCCESS_MODEL_DISABLED);
        return 1;
    }
}
