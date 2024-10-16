package com.codingcat.modelshifter.client.gui.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.api.renderer.AdditionalRendererState;
import com.codingcat.modelshifter.client.api.skin.SingleAsyncSkinProvider;
import com.codingcat.modelshifter.client.gui.widget.ModelPreviewButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.ModelShifterButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.MultiOptionButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.PlayerShowcaseWidget;
import com.codingcat.modelshifter.client.impl.option.ModeOption;
import com.codingcat.modelshifter.client.impl.skin.SingleAsyncSkinProviderImpl;
import com.codingcat.modelshifter.client.util.Util;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ModelSelectionScreen extends AbstractCustomGameOptionsScreen {
    private static final Text TITLE = Text.translatable("modelshifter.screen.model_selection.title");
    private static final Text DISPLAY_MODE_BUTTON = Text.translatable("modelshifter.button.display_mode");
    private static final Text PLAYER_OVERRIDES_BUTTON = Text.translatable("modelshifter.button.player_overrides");
    private static final BiFunction<Integer, Integer, Text> PAGE_BUTTON = (page, count) -> Text.translatable("modelshifter.button.page", page, count);
    private static final Function<String, Text> TITLE_PLAYER = name -> Text.translatable("modelshifter.screen.model_selection.title_player", name);
    private int pageCount;
    private int currentPage;

    private PlayerShowcaseWidget previewWidget;
    @Nullable
    private final GameProfile targetPlayer;
    private final SingleAsyncSkinProvider skinProvider;

    public ModelSelectionScreen(@Nullable GameProfile targetPlayer, Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, targetPlayer != null ? TITLE_PLAYER.apply(targetPlayer.getName()) : TITLE);
        this.targetPlayer = targetPlayer;
        this.skinProvider = new SingleAsyncSkinProviderImpl(this.obtainPlayer());
    }

    @Override
    protected void init() {
        this.skinProvider.fetchSkin();
        initialize();
        super.init();
    }

    private void initialize() {
        if (client == null) return;

        this.previewWidget = this.addPlayerPreview();
        this.addButton(0, 0, -1, null);
        this.pageCount = 1;
        this.currentPage = 0;
        int x = 0;
        int y = 0;
        int page = 0;
        for (Pair<Identifier, PlayerModel> model : ModelRegistry.entriesSorted()) {
            x++;
            if (checkLineBreak(x)) {
                x = 0;
                y++;
                if (checkPageBreak(y)) {
                    this.pageCount++;
                    page++;
                    x = 1;
                    y = 0;
                }
            }

            this.addButton(x, y, page, model.getValue());
        }

        if (this.targetPlayer == null)
            this.addTopButtons();

        ButtonWidget pageButton = ButtonWidget.builder(getPageText(), button -> {
                    if ((currentPage + 1) >= pageCount)
                        currentPage = 0;
                    else
                        currentPage++;

                    button.setMessage(getPageText());
                    updatePage();
                })
                .dimensions(width - 85, height - 26, 80, 20)
                .build();

        pageButton.active = pageCount > 1;
        this.addDrawableChild(pageButton);
    }

    private boolean checkLineBreak(int x) {
        return (x * 80) + 24 > width / 1.8;
    }

    private boolean checkPageBreak(int y) {
        return (y * 80) + 45 > height - 100;
    }

    private AdditionalRendererState obtainState() {
        return this.targetPlayer != null ? ModelShifterClient.state.getState(this.targetPlayer.getId()) : ModelShifterClient.state.getGlobalState();
    }

    @NotNull
    private GameProfile obtainPlayer() {
        return this.targetPlayer != null ? this.targetPlayer : Util.getGameProfile();
    }

    private void setState(boolean rendererEnabled, @Nullable PlayerModel model) {
        if (this.targetPlayer != null)
            ModelShifterClient.state.setPlayerState(this.targetPlayer.getId(), rendererEnabled, model);
        else
            ModelShifterClient.state.setGlobalState(rendererEnabled, model);

        ModelShifterClient.holder.applyState();
    }

    private Text getPageText() {
        return PAGE_BUTTON.apply(currentPage + 1, pageCount);
    }

    private void addTopButtons() {
        if (client == null) return;

        int w = Math.min((int) (width / 3.3d), 250);
        MultiOptionButtonWidget<ModeOption> displayModeButton = new MultiOptionButtonWidget<>(
                width - (w + 5),
                5,
                w,
                20,
                DISPLAY_MODE_BUTTON,
                ModeOption.OPTIONS,
                () -> ModelShifterClient.state.getDisplayMode(),
                modeOption -> {
                    ModelShifterClient.state.setDisplayMode(modeOption);
                    ModelShifterClient.holder.applyState();

                    return modeOption.getDisplayName();
                });

        ModelShifterButtonWidget playerOverridesButton = new ModelShifterButtonWidget(
                displayModeButton.getX() - 25,
                5,
                PLAYER_OVERRIDES_BUTTON,
                true,
                btn -> client.setScreen(new PlayerOverridesScreen(this, gameOptions))
        );

        this.addDrawableChild(playerOverridesButton);
        this.addDrawableChild(displayModeButton);
    }

    private PlayerShowcaseWidget addPlayerPreview() {
        PlayerShowcaseWidget previewWidget = new PlayerShowcaseWidget(
                obtainPlayer(),
                this.skinProvider,
                PlayerShowcaseWidget.TextMode.MODEL,
                width / 2, 0,
                width / 2, height);

        return this.addDrawableChild(previewWidget);
    }

    private void addButton(int posX, int posY, int page, @Nullable PlayerModel model) {
        ModelPreviewButtonWidget buttonWidget = new ModelPreviewButtonWidget(
                (posX * 80) + 24,
                (posY * 80) + 45,
                65,
                model != null ? ModelPreviewButtonWidget.Type.NORMAL : ModelPreviewButtonWidget.Type.DISABLE_BUTTON,
                model, this.skinProvider,
                this::onButtonSelect,
                () -> page == -1 || (currentPage == page));
        if ((model != null && obtainState().isRendererEnabled() && Objects.requireNonNull(obtainState().getPlayerModel()).equals(model))
                || (model == null && !obtainState().isRendererEnabled())) {
            buttonWidget.setSelected(true);
            this.currentPage = page;
            this.updatePage();
        }

        this.addDrawableChild(buttonWidget);
    }

    private void updatePage() {
        for (Element element : this.children()) {
            if (!(element instanceof ModelPreviewButtonWidget widget)) continue;
            widget.updateVisibility();
        }
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
}