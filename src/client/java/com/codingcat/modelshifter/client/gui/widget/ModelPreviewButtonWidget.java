package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.impl.Models;
import com.codingcat.modelshifter.client.render.GuiPlayerEntityRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class ModelPreviewButtonWidget extends PressableWidget {
    private static final ButtonTextures TEXTURES = new ButtonTextures(id("model_button"), id("model_button_active"));
    private final PlayerModel model;
    private final GuiPlayerEntityRenderer renderer;
    private final Identifier skinTexture;
    private final Consumer<ModelPreviewButtonWidget> onPressConsumer;
    private boolean selected;

    public ModelPreviewButtonWidget(int x, int y, int size, PlayerModel model, Consumer<ModelPreviewButtonWidget> onPress) {
        super(x, y, size, size, Text.empty());
        this.model = model;
        this.renderer = new GuiPlayerEntityRenderer(model.getModelDataIdentifier());
        MinecraftClient client = MinecraftClient.getInstance();
        this.skinTexture = client.getSkinProvider().getSkinTextures(client.getGameProfile()).texture();
        this.onPressConsumer = onPress;
        this.selected = false;
        this.setTooltip(Tooltip.of(getModelName()));
    }

    private static Identifier id(String id) {
        return new Identifier(ModelShifterClient.MOD_ID, String.format("widget/%s", id));
    }

    private Text getModelName() {
        String translationKey = Models.getTranslationKey(model);
        return Text.translatable(translationKey);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void onPress() {
        this.onPressConsumer.accept(this);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderButton(context);
        this.renderModel(context);
    }

    private void renderButton(DrawContext context) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(TEXTURES.get(this.active, this.selected), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    private void renderModel(DrawContext context) {
        MatrixStack matrices = context.getMatrices();
        context.enableScissor(getX()+2, getY()+2, (getX() + getWidth())-2, (getY() + getHeight())-2);
        matrices.push();
        matrices.translate(getX() + (getWidth() / 2f), getY() + (getHeight() / 1.2f), 50);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateY((float) (Math.PI * ((double) System.currentTimeMillis() / 2600f) % 360f));
        quaternionf.mul(quaternionf2);
        matrices.multiply(quaternionf);
        float size = getHeight() / 2.5f;
        matrices.scale(size, size, -size);
        renderer.render(skinTexture, 0, 0, matrices, context.getVertexConsumers(), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
        context.draw();
        matrices.pop();
        context.disableScissor();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, getModelName());
    }
}
