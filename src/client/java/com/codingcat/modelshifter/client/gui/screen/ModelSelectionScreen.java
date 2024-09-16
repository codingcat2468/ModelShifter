package com.codingcat.modelshifter.client.gui.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.gui.widget.ModelPreviewButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.ModelShifterButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.MultiOptionButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.PlayerShowcaseWidget;
import com.codingcat.modelshifter.client.impl.config.Configuration;
import com.codingcat.modelshifter.client.impl.config.ConfigurationLoader;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
//? <1.21 {
/*import net.minecraft.client.gui.widget.OptionListWidget;
 *///?}
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class ModelSelectionScreen extends GameOptionsScreen {
    private static final Text TITLE = Text.translatable("modelshifter.screen.model_selection.title");
    private static final Text DISPLAY_MODE_BUTTON = Text.translatable("modelshifter.button.display_mode");
    private static final Text PLAYER_OVERRIDES_BUTTON = Text.translatable("modelshifter.button.player_overrides");
    private static final Function<String, Text> TITLE_PLAYER = name -> Text.translatable("modelshifter.screen.model_selection.title_player", name);

    //? <1.21 {
    /*private OptionListWidget listWidget;
     *///?}
    private PlayerShowcaseWidget previewWidget;
    @Nullable
    private final GameProfile targetPlayer;
    @NotNull
    private final AtomicReference<Identifier> skinTexture;

    public ModelSelectionScreen(@Nullable GameProfile targetPlayer, Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, targetPlayer != null ? TITLE_PLAYER.apply(targetPlayer.getName()) : TITLE);
        this.targetPlayer = targetPlayer;
        MinecraftClient client1 = MinecraftClient.getInstance();
        Identifier defaultTexture = client1.getSkinProvider().getSkinTextures(targetPlayer != null ? targetPlayer
                : client1.getGameProfile()).texture();
        this.skinTexture = new AtomicReference<>(defaultTexture);
    }

    @Override
    protected void init() {
        fetchSkin();
        initialize();
        super.init();
    }

    private void initialize() {
        if (client == null) return;

        //? <1.21 {
        /*this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height, this));
         *///?}
        this.previewWidget = this.addPlayerPreview();
        this.addButton(0, 0, null);
        int x = 1;
        int y = 0;
        for (Pair<Identifier, PlayerModel> model : ModelRegistry.entriesSorted()) {
            this.addButton(x, y, model.getValue());
            x++;
            if ((x * 80) + 24 > width / 1.8) {
                x = 0;
                y++;
            }
        }

        if (this.targetPlayer != null) return;

        ModelShifterButtonWidget playerOverridesButton = new ModelShifterButtonWidget(
                width - 230,
                5,
                PLAYER_OVERRIDES_BUTTON,
                true,
                btn -> client.setScreen(new PlayerOverridesScreen(this, gameOptions))
        );

        ConfigurationLoader loader = new ConfigurationLoader();
        Configuration config = loader.load(Configuration.class);
        MultiOptionButtonWidget<ModeOption> displayModeButton = new MultiOptionButtonWidget<>(
                width - 205,
                5,
                200,
                20,
                DISPLAY_MODE_BUTTON,
                ModeOption.OPTIONS,
                () -> ModeOption.byId(config.getDisplayMode()),
                modeOption -> {
                    config.setDisplayMode(modeOption.getId());
                    loader.write(config);
                    ModelShifterClient.state.setDisplayMode(modeOption);

                    return modeOption.getDisplayName();
                });

        this.addDrawableChild(playerOverridesButton);
        this.addDrawableChild(displayModeButton);
    }

    private void fetchSkin() {
        if (client == null) return;
        CompletableFuture<SkinTextures> texturesFuture = client.getSkinProvider()
                .fetchSkinTextures(targetPlayer != null ? targetPlayer : client.getGameProfile());
        texturesFuture.thenAccept(textures -> skinTexture.set(textures.texture()));
    }

    private AdditionalRendererState obtainState() {
        return this.targetPlayer != null ? ModelShifterClient.state.getState(this.targetPlayer.getId()) : ModelShifterClient.state.getGlobalState();
    }

    private void setState(boolean rendererEnabled, @Nullable PlayerModel model) {
        if (this.targetPlayer != null)
            ModelShifterClient.state.setPlayerState(this.targetPlayer.getId(), rendererEnabled, model);
        else
            ModelShifterClient.state.setGlobalState(rendererEnabled, model);

        ModelShifterClient.holder.applyState();
    }

    //? >=1.21 {
    @Override
    protected void addOptions() {
    }
    //?}

    private PlayerShowcaseWidget addPlayerPreview() {
        GameProfile player = MinecraftClient.getInstance().getGameProfile();
        PlayerShowcaseWidget previewWidget = new PlayerShowcaseWidget(
                targetPlayer != null ? targetPlayer : player,
                skinTexture,
                PlayerShowcaseWidget.TextMode.MODEL,
                width / 2, 0,
                width / 2, height);

        return this.addDrawableChild(previewWidget);
    }

    private void addButton(int posX, int posY, @Nullable PlayerModel model) {
        ModelPreviewButtonWidget buttonWidget = new ModelPreviewButtonWidget(
                (posX * 80) + 24,
                (posY * 80) + 45,
                65,
                model != null ? ModelPreviewButtonWidget.Type.NORMAL : ModelPreviewButtonWidget.Type.DISABLE_BUTTON,
                model, skinTexture,
                this::onButtonSelect);
        if ((model != null && obtainState().isRendererEnabled() && Objects.requireNonNull(obtainState().getPlayerModel()).equals(model))
                || (model == null && !obtainState().isRendererEnabled()))
            buttonWidget.setSelected(true);
        this.addDrawableChild(buttonWidget);
    }

    private void onButtonSelect(ModelPreviewButtonWidget buttonWidget) {
        if (client == null || buttonWidget.isButtonSelected()) return;

        unselectAll();
        buttonWidget.setSelected(true);
        setState(buttonWidget.getModel() != null, buttonWidget.getModel());
        previewWidget.update();
    }

    private void unselectAll() {
        for (Element element : this.children()) {
            if (!(element instanceof ModelPreviewButtonWidget widget)) continue;

            widget.setSelected(false);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        //? <1.21 {
        /*this.clearChildren();
        this.init();
        *///?} else {
        if (this.client != null)
            this.client.setScreen(new ModelSelectionScreen(targetPlayer, parent, gameOptions));
        //?}
    }

    //? <1.21 {

    /*protected void initTabNavigation() {
        super.initTabNavigation();
        if (this.listWidget != null)
            this.listWidget.position(this.width, this.layout);
     }
        *///?}
}
