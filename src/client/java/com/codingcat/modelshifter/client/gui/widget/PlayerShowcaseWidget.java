package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.impl.Models;
import com.codingcat.modelshifter.client.render.GuiPlayerEntityRenderer;
import com.codingcat.modelshifter.client.util.Util;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlayerShowcaseWidget extends TextWidget {
    private static final Identifier BACKGROUND = Identifier.of(ModelShifterClient.MOD_ID, "widget/preview_background");
    @Nullable
    private GuiPlayerEntityRenderer renderer;
    @NotNull
    private final AtomicReference<Identifier> skinTexture;
    @NotNull
    private GameProfile gameProfile;
    private final PlayerEntityModel<?> playerEntityModel;
    private final TextMode textMode;
    private boolean contentVisible;

    public PlayerShowcaseWidget(@NotNull GameProfile player, @NotNull AtomicReference<Identifier> skinTexture, TextMode textMode, int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty(), MinecraftClient.getInstance().textRenderer);
        this.skinTexture = skinTexture;
        this.playerEntityModel = createModel();
        this.gameProfile = player;
        this.textMode = textMode;
        this.contentVisible = true;
        this.update();
    }

    public void setContentVisible(boolean contentVisible) {
        this.contentVisible = contentVisible;
    }

    public void setPlayer(GameProfile player, Identifier skinTexture) {
        this.gameProfile = player;
        this.skinTexture.set(skinTexture);
        this.update();
    }

    private PlayerModel getPlayerModel() {
        return ModelShifterClient.state.getState(gameProfile.getId()).getPlayerModel();
    }

    private Text getModelName() {
        PlayerModel model = getPlayerModel();
        if (model == null)
            return Text.translatable("modelshifter.state.no_custom_model");

        String translationKey = Models.getTranslationKey(model);
        return Text.translatable(translationKey);
    }

    public void update() {
        PlayerModel model = getPlayerModel();
        if (model == null || !ModelShifterClient.state.isRendererStateEnabled(gameProfile.getId())) return;

        this.renderer = new GuiPlayerEntityRenderer(model.getModelDataIdentifier(), model.getGuiRenderInfo().getShowcaseAnimation());
    }

    //? <=1.20.1 {
    /*@Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderWidget(context, mouseX, mouseY, delta);
    }
    *///?}

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (!this.contentVisible) return;

        this.renderModel(context);
        this.renderText(context);
    }

    private void renderBackground(DrawContext context) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Util.drawGuiTexture(context, BACKGROUND,
                getX() + getWidth() - (getWidth() / 2) - 16,
                this.getY() + 32,
                this.getWidth(),
                this.getHeight() - 64);
    }

    private void renderText(DrawContext context) {
        float textSizeMultiplier = width / 320f;
        renderScaledText(context,
                getText(true),
                0xFFFFFF,
                getX() + getWidth() - (getWidth() / 4f),
                getY() + getHeight() - (getHeight() / 3f),
                2f * textSizeMultiplier, true);
        renderScaledText(context,
                getText(false),
                0xADADAD,
                getX() + getWidth() - (getWidth() / 4f),
                getY() + getHeight() - (getHeight() / 3f) + (24 * textSizeMultiplier),
                textSizeMultiplier, true);
    }

    public static void renderScaledText(DrawContext context, Text text, int color, double x, double y, float scale, boolean centered) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, scale);
        if (centered)
            context.drawCenteredTextWithShadow(textRenderer, text, 0, 0, color);
        else
            context.drawTextWithShadow(textRenderer, text, 0, 0, color);
        context.getMatrices().pop();
    }

    private void renderModel(DrawContext context) {
        if (skinTexture.get() == null) return;

        MatrixStack matrices = context.getMatrices();
        context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        matrices.push();
        matrices.translate(getX() + getWidth() - (getWidth() / 4f), getY() + ((float) height / 1.6), 50);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateY((float) (Math.PI * 0.25f));
        quaternionf.mul(quaternionf2);
        matrices.multiply(quaternionf);
        float size = getHeight() / 4f;
        if (ModelShifterClient.state.isRendererStateEnabled(gameProfile.getId()) && renderer != null) {
            PlayerModel model = getPlayerModel();
            assert model != null;
            Consumer<MatrixStack> tweakFunction = model.getGuiRenderInfo().getShowcaseRenderTweakFunction();
            matrices.scale(size, size, -size);
            if (tweakFunction != null)
                tweakFunction.accept(matrices);
            renderer.setRenderColor(255, 255, 255, 255);
            renderer.render(skinTexture.get(), 0, 0, matrices, context.getVertexConsumers(), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
        } else if (!ModelShifterClient.state.isRendererStateEnabled(gameProfile.getId()) && playerEntityModel != null) {
            size /= 1.2f;
            matrices.scale(size, size, -size);
            matrices.translate(0, 1.4f, 0);
            matrices.multiply(new Quaternionf().rotateZ((float) Math.PI));
            VertexConsumerProvider.Immediate vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            VertexConsumer consumer = vertexConsumer.getBuffer(RenderLayer.getEntityTranslucent(skinTexture.get()));
            int overlay = OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(false));

            playerEntityModel.render(matrices,
                    consumer,
                    LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                    overlay,
                    //? <1.21 {
                    /*1f, 1f, 1f, 1f
                    *///?} else {
                    -1
                     //?}
            );
        }

        context.draw();
        matrices.pop();
        context.disableScissor();
    }

    private Text getText(boolean top) {
        boolean active = ModelShifterClient.state.isRendererStateEnabled(gameProfile.getId());
        PlayerModel model = getPlayerModel();
        Text modelName = getModelName();
        String creators = (active && model != null) ? String.join(", ", model.getCreators()) : "";
        if (top)
            return (textMode == TextMode.MODEL) ? modelName
                    : Text.literal(gameProfile.getName());
        else
            return (textMode == TextMode.MODEL) ? Text.translatable(active ? "modelshifter.text.made_by" : "modelshifter.text.no_model_active", creators)
                    : Text.translatable(active ? "modelshifter.text.model_info" : "modelshifter.text.no_model_active", modelName);
    }

    private PlayerEntityModel<?> createModel() {
        ModelData data = PlayerEntityModel.getTexturedModelData(Dilation.NONE, false);
        ModelPart root = TexturedModelData.of(data, 64, 64).createModel();
        PlayerEntityModel<?> model = new PlayerEntityModel<>(root, false);
        model.child = false;

        return model;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, getModelName());
    }

    public enum TextMode {
        MODEL,
        PLAYER
    }
}