package com.codingcat.modelshifter.client.gui.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.gui.widget.ErrorTextFieldWidget;
import com.codingcat.modelshifter.client.gui.widget.PlayerOverridesListWidget;
import com.codingcat.modelshifter.client.gui.widget.PlayerShowcaseWidget;
import com.codingcat.modelshifter.client.gui.widget.entry.PlayerOverridesListWidgetEntry;
import com.codingcat.modelshifter.client.impl.config.ConfigPlayerOverride;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.resolver.MojangResolver;
import com.github.games647.craftapi.resolver.RateLimitException;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlayerOverridesScreen extends AbstractCustomGameOptionsScreen {
    private static final Text TITLE = Text.translatable("modelshifter.screen.player_overrides.title");
    private static final Text ENTER_PLAYER_NAME = Text.translatable("modelshifter.text.enter_player_name");
    private static final Text ADD_PLAYER_BUTTON = Text.translatable("modelshifter.button.add_player");
    private static final Text REMOVE_PLAYER_BUTTON = Text.translatable("modelshifter.button.remove_player");
    private static final Text SELECT_MODEL_BUTTON = Text.translatable("modelshifter.button.select_model");
    private static final Text ERROR_OBTAIN_PLAYER = Text.translatable("modelshifter.error.player.other");
    private static final Text ERROR_PLAYER_NOT_FOUND = Text.translatable("modelshifter.error.player.not_found");
    @Nullable
    private GameProfile selectedPlayer;
    private PlayerOverridesListWidget overridesListWidget;
    private ErrorTextFieldWidget playerNameWidget;
    private PlayerShowcaseWidget previewWidget;
    private List<ButtonWidget> selectionDependentButtons;


    public PlayerOverridesScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE);
    }

    @Override
    protected void init() {
        initialize();
        super.init();
    }

    private void initialize() {
        if (client == null) return;
        this.playerNameWidget = new ErrorTextFieldWidget(
                client.textRenderer,
                (width / 3) + 20,
                45,
                width / 3,
                20,
                ENTER_PLAYER_NAME);

        ButtonWidget addPlayerButton = ButtonWidget.builder(ADD_PLAYER_BUTTON, this::tryAddPlayer)
                .dimensions((width / 3) + 20, 70, width / 3, 20)
                .build();

        ButtonWidget changeModelButton = ButtonWidget.builder(SELECT_MODEL_BUTTON, button -> {
                    if (selectedPlayer != null)
                        client.setScreen(new ModelSelectionScreen(selectedPlayer, this, gameOptions));
                })
                .dimensions((width / 3) + 20, 93, (width / 6) - 2, 20)
                .build();

        ButtonWidget removeOverrideButton = ButtonWidget.builder(REMOVE_PLAYER_BUTTON, button -> {
                    if (selectedPlayer == null) return;
                    modifyOverrides(overrides -> overrides.removeIf(override -> override.player().equals(selectedPlayer.getId())));
                    reloadOverrides();
                    changeSelection(null);
                })
                .dimensions((width / 3) + (width / 6) + 23, 93, (width / 6) - 2, 20)
                .build();

        this.selectionDependentButtons = List.of(changeModelButton, removeOverrideButton);
        this.addPlayerPreview();
        this.addList();
        this.addDrawableChild(playerNameWidget);
        this.addDrawableChild(addPlayerButton);
        this.addDrawableChild(changeModelButton);
        this.addDrawableChild(removeOverrideButton);
        this.changeSelection(null);
    }

    private void addList() {
        this.overridesListWidget = new PlayerOverridesListWidget(
                this.client,
                width / 3,
                height - 70,
                5,
                35,
                40,
                this::changeSelection
        );

        this.reloadOverrides();
        this.addDrawableChild(overridesListWidget);
    }

    private void changeSelection(PlayerOverridesListWidgetEntry entry) {
        this.previewWidget.setContentVisible(entry != null);
        this.selectionDependentButtons.forEach(button -> button.active = entry != null);
        if (entry == null) {
            this.selectedPlayer = null;
            return;
        }

        this.selectedPlayer = entry.getPlayerProfile();
        this.previewWidget.setPlayer(selectedPlayer, entry.getSkinTexture());
    }

    private void addPlayerPreview() {
        GameProfile player = MinecraftClient.getInstance().getGameProfile();
        this.previewWidget = new PlayerShowcaseWidget(
                player, new AtomicReference<>(),
                PlayerShowcaseWidget.TextMode.PLAYER,
                width / 2, 0,
                width / 2, height);

        this.addDrawableChild(previewWidget);
    }

    private void tryAddPlayer(ButtonWidget buttonWidget) {
        String name = playerNameWidget.getText();
        if (name.isBlank()) return;
        new Thread(() -> {
            Optional<GameProfile> profile = this.queryProfile(name);
            if (profile.isEmpty()) {
                playerNameWidget.setError(ERROR_PLAYER_NOT_FOUND);
                return;
            }

            playerNameWidget.setText("");
            modifyOverrides(overrides -> {
                if (overrides
                        .stream()
                        .noneMatch(override -> override.player().equals(profile.get().getId())))
                    overrides.add(new ConfigPlayerOverride(profile.get().getId(), new AdditionalRendererState()));
            });
            reloadOverrides();
        }, "Profile Fetcher Thread").start();
    }

    private void modifyOverrides(Consumer<ArrayList<ConfigPlayerOverride>> overridesSupplier) {
        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration config = loader.load(Configuration.class);
        overridesSupplier.accept(config.getPlayerOverrides());
        loader.write(config);
        ModelShifterClient.state.reloadFromOverrides(new HashSet<>(config.getPlayerOverrides()));
    }

    private void reloadOverrides() {
        ArrayList<ConfigPlayerOverride> overrides = new ConfigurationLoader()
                .load(Configuration.class)
                .getPlayerOverrides();

        overridesListWidget.setOverrides(overrides);
        if (overrides.isEmpty())
            changeSelection(null);
        previewWidget.update();
    }

    private Optional<GameProfile> queryProfile(String name) {
        MojangResolver resolver = new MojangResolver();
        try {
            Optional<Profile> profile = resolver.findProfile(name);
            return profile.map(playerProfile -> new GameProfile(playerProfile.getId(), name));
        } catch (IOException | RateLimitException e) {
            playerNameWidget.setError(ERROR_OBTAIN_PLAYER);
            return Optional.empty();
        }
    }

    @Override
    protected void initTabNavigation() {
        super.initTabNavigation();
        reloadOverrides();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        //? <1.21 {
        /*this.clearChildren();
        this.init();
        *///?} else {
        if (this.client != null)
            this.client.setScreen(new PlayerOverridesScreen(parent, gameOptions));
        //?}
    }
}
