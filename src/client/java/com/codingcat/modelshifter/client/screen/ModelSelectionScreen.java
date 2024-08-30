package com.codingcat.modelshifter.client.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.registry.ModelRegistry;
import com.codingcat.modelshifter.client.impl.model.Models;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Map;

public class ModelSelectionScreen extends GameOptionsScreen {
    private static final Text TITLE = Text.translatable("modelshifter.screen.model_selection.title");

    private OptionListWidget listWidget;

    public ModelSelectionScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, TITLE);
    }

    @Override
    protected void init() {
        this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height, this));
        ArrayList<ClickableWidget> list = new ArrayList<>();
        for (Map.Entry<Identifier, PlayerModel> model : ModelRegistry.entries()) {
            String translationKey = Models.getTranslationKey(model.getKey());
            ButtonWidget btn = ButtonWidget.builder(Text.translatable(translationKey), button -> {
                if (client == null) return;

                ClientPlayerEntity player = client.player;
                if (player != null)
                    player.sendMessage(Text.translatable("modelshifter.message.model_changed", Text.translatable(translationKey)));
                client.setScreen(null);

                ModelShifterClient.state.setState(true, model.getValue());
                ModelShifterClient.holder.applyState();
            }).build();
            list.add(btn);
        }

        listWidget.addAll(list);
        super.init();
    }

    @Override
    protected void initTabNavigation() {
        super.initTabNavigation();
        if (this.listWidget != null)
            this.listWidget.position(this.width, this.layout);
    }
}
