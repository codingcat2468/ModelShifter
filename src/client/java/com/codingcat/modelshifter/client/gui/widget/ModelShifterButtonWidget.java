package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModelShifterButtonWidget extends PressableWidget {
    private static final Text MODEL_SHIFTER_BTN = Text.translatable("modelshifter.button.open_screen");
    private static final ButtonTextures TEXTURES = new ButtonTextures(id("modelshifter_button"), id("modelshifter_button_focused"));
    private final Consumer<ModelShifterButtonWidget> onPress;

    public ModelShifterButtonWidget(int x, int y, Consumer<ModelShifterButtonWidget> onPress) {
        super(x, y, 20, 20, Text.empty());
        this.onPress = onPress;
        this.setTooltip(Tooltip.of(MODEL_SHIFTER_BTN));
    }

    private static Identifier id(String id) {
        return Identifier.of(ModelShifterClient.MOD_ID, String.format("icons/%s", id));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(TEXTURES.get(this.active, this.isSelected()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onPress() {
        this.onPress.accept(this);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, MODEL_SHIFTER_BTN);
    }
}
