package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.impl.Models;
import com.codingcat.modelshifter.client.render.GuiPlayerEntityRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class PlayerPreviewWidget extends TextWidget {
    private static final Identifier BACKGROUND = new Identifier(ModelShifterClient.MOD_ID, "widget/preview_background");
    @Nullable
    private GuiPlayerEntityRenderer renderer;
    private final Identifier skinTexture;

    public PlayerPreviewWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty(), MinecraftClient.getInstance().textRenderer);
        MinecraftClient client = MinecraftClient.getInstance();
        this.skinTexture = client.getSkinProvider().getSkinTextures(client.getGameProfile()).texture();
        this.update();
    }

    private Text getModelName() {
        PlayerModel model = ModelShifterClient.state.getPlayerModel();
        if (model == null)
            return Text.translatable("modelshifter.state.no_custom_model");

        String translationKey = Models.getTranslationKey(model);
        return Text.translatable(translationKey);
    }

    public void update() {
        PlayerModel model = ModelShifterClient.state.getPlayerModel();
        if (model == null || !ModelShifterClient.state.isRendererEnabled()) return;

        this.renderer = new GuiPlayerEntityRenderer(model.getModelDataIdentifier(), false);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (ModelShifterClient.state.isRendererEnabled())
            this.renderModel(context);
        this.renderText(context);
    }

    private void renderBackground(DrawContext context) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawGuiTexture(BACKGROUND,
                getX() + getWidth() - (getWidth() / 2) - 16,
                this.getY() + 32,
                this.getWidth(),
                this.getHeight() - 64);
    }

    private void renderText(DrawContext context) {
        this.renderScaledText(context,
                getModelName(),
                0xFFFFFF,
                getX() + getWidth() - (getWidth() / 4f),
                getY() + getHeight() - (getHeight() / 3f),
                2f);
        boolean active = ModelShifterClient.state.isRendererEnabled();
        PlayerModel model = ModelShifterClient.state.getPlayerModel();
        String creators = (active && model != null) ? String.join(", ", model.getCreators()) : "";
        this.renderScaledText(context,
                Text.translatable(active ? "modelshifter.text.made_by" : "modelshifter.text.no_model_active", creators),
                0xADADAD,
                getX() + getWidth() - (getWidth() / 4f),
                getY() + getHeight() - (getHeight() / 3f) + 24,
                1f);
    }

    private void renderScaledText(DrawContext context, Text text, int color, double x, double y, float scale) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, scale);
        context.drawCenteredTextWithShadow(textRenderer, text, 0, 0, color);
        context.getMatrices().pop();
    }

    private void renderModel(DrawContext context) {
        if (renderer == null) return;

        MatrixStack matrices = context.getMatrices();
        context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        matrices.push();
        matrices.translate(getX() + getWidth() - (getWidth() / 4f), getY() + ((float) height / 1.6), 50);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateY((float) (Math.PI * 0.25f));
        quaternionf.mul(quaternionf2);
        matrices.multiply(quaternionf);
        float size = getHeight() / 4f;
        matrices.scale(size, size, -size);
        renderer.setRenderColor(255, 255, 255, 255);
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