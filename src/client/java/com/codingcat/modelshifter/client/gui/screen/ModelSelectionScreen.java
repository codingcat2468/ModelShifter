package com.codingcat.modelshifter.client.gui.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.gui.widget.ModelPreviewButtonWidget;
import com.codingcat.modelshifter.client.gui.widget.PlayerPreviewWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class ModelSelectionScreen extends GameOptionsScreen {
    private static final Text TITLE = Text.translatable("modelshifter.screen.model_selection.title");

    //? if <1.21 {
    //private OptionListWidget listWidget;
    //?}
    private PlayerPreviewWidget previewWidget;

    public ModelSelectionScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE);
    }

    @Override
    protected void init() {
        initialize();
        super.init();
    }

    private void initialize() {
        if (client == null) return;

        //? if <1.21 {
        //this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height, this));
        //?}
        this.previewWidget = this.addPlayerPreview();
        this.addButton(0, 0, null);
        int x = 1;
        int y = 0;
        for (Map.Entry<Identifier, PlayerModel> model : ModelRegistry.entries()) {
            this.addButton(x, y, model.getValue());
            x++;
            if ((x * 80) + 24 > width / 1.8) {
                x = 0;
                y++;
            }
        }
    }

    //? if >=1.21 {
    @Override
    protected void addOptions() {
    }
    //?}

    private PlayerPreviewWidget addPlayerPreview() {
        PlayerPreviewWidget previewWidget = new PlayerPreviewWidget(
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
                model,
                this::onButtonSelect);
        if ((model != null && ModelShifterClient.state.isRendererEnabled() && Objects.requireNonNull(ModelShifterClient.state.getPlayerModel()).equals(model))
                || (model == null && !ModelShifterClient.state.isRendererEnabled()))
            buttonWidget.setSelected(true);
        this.addDrawableChild(buttonWidget);
    }

    private void onButtonSelect(ModelPreviewButtonWidget buttonWidget) {
        if (client == null) return;

        unselectAll();
        buttonWidget.setSelected(true);
        ModelShifterClient.state.setState(buttonWidget.getModel() != null, buttonWidget.getModel());
        ModelShifterClient.holder.applyState();
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
        //? if <1.21 {
        /*this.clearChildren();
        this.init();
        *///?} else {
        if (this.client != null)
            this.client.setScreen(new ModelSelectionScreen(parent, gameOptions));
        //?}
    }

    //? if <1.21 {
    //@Override
    //protected void initTabNavigation() {
    //    super.initTabNavigation();
    //    if (this.listWidget != null)
    //        this.listWidget.position(this.width, this.layout);
    //}
    //?}
}
