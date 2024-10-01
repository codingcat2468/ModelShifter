package com.codingcat.modelshifter.client.commands.argument;

import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModelIdentifierArgumentType implements ArgumentType<Optional<Identifier>> {
    private static final DynamicCommandExceptionType MODEL_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(id -> Text.translatable("modelshifter.commmand.model.error.not_found", id));
    private static final String DISABLE_ARGUMENT = "disable";

    @Override
    public Optional<Identifier> parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        if (reader.readString().equals(DISABLE_ARGUMENT)) return Optional.empty();

        reader.setCursor(cursor);
        return Optional.of(Identifier.fromCommandInput(reader));
    }

    public static ModelIdentifierArgumentType modelIdentifier() {
        return new ModelIdentifierArgumentType();
    }

    @SuppressWarnings("unchecked")
    public static Optional<Identifier> getIdentifier(CommandContext<FabricClientCommandSource> context, String name) {
        return (Optional<Identifier>) context.getArgument(name, Optional.class);
    }

    @Nullable
    public static PlayerModel getModel(CommandContext<FabricClientCommandSource> context, String name) throws CommandSyntaxException {
        Optional<Identifier> identifier = ModelIdentifierArgumentType.getIdentifier(context, name);
        if (identifier.isEmpty()) return null;

        Optional<PlayerModel> model = ModelRegistry.get(identifier.get());
        if (model.isEmpty())
            throw MODEL_NOT_FOUND_EXCEPTION.create(identifier.get());

        return model.get();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemainingLowerCase();
        if (DISABLE_ARGUMENT.startsWith(input))
            builder.suggest(DISABLE_ARGUMENT);

        ModelRegistry.entries()
                .stream()
                .filter(entry -> {
                    Identifier id = entry.getKey().getValue();
                    return id.getPath().startsWith(input) || id.getNamespace().startsWith(input) || id.toString().startsWith(input);
                })
                .forEach(entry -> builder.suggest(entry.getKey().getValue().toString()));

        return builder.buildFuture();
    }
}
