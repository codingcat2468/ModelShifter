package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModelShifterButtonWidget extends PressableWidget {
    private static final Identifier TEXTURE = id("modelshifter_button");
    private static final Identifier TEXTURE_FOCUSED = id("modelshifter_button_focused");
    private static final Identifier TEXTURES_PLAYERS = id("modelshifter_button_players");
    private static final Identifier TEXTURES_PLAYERS_FOCUSED = id("modelshifter_button_players_focused");
    private final Consumer<ModelShifterButtonWidget> onPress;
    private final Text tooltip;
    private final boolean isPlayersButton;

    public ModelShifterButtonWidget(int x, int y, Text tooltip, boolean isPlayersButton, Consumer<ModelShifterButtonWidget> onPress) {
        super(x, y, 20, 20, Text.empty());
        this.onPress = onPress;
        this.tooltip = tooltip;
        this.isPlayersButton = isPlayersButton;
        this.setTooltip(Tooltip.of(tooltip));
    }

    private static Identifier id(String id) {
        return Identifier.of(ModelShifterClient.MOD_ID, String.format("icons/%s", id));
    }

    //? <=1.20.1 {
    /*@Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderWidget(context, mouseX, mouseY, delta);
    }
    *///?}

    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Identifier texture = this.isPlayersButton ?
                (this.isSelected() ? TEXTURES_PLAYERS_FOCUSED : TEXTURES_PLAYERS) :
                (this.isSelected() ? TEXTURE_FOCUSED : TEXTURE);

        Util.drawGuiTexture(context, texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onPress() {
        this.onPress.accept(this);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, this.tooltip);
    }
}
