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
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class PlayerShowcaseWidget extends TextWidget {
    private static final Identifier BACKGROUND = Identifier.of(ModelShifterClient.MOD_ID, "widget/preview_background");
    @Nullable
    private GuiPlayerEntityRenderer renderer;
    private final Identifier skinTexture;
    private final PlayerEntityModel<?> playerEntityModel;

    public PlayerShowcaseWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty(), MinecraftClient.getInstance().textRenderer);
        MinecraftClient client = MinecraftClient.getInstance();
        this.skinTexture = client.getSkinProvider().getSkinTextures(client.getGameProfile()).texture();
        this.playerEntityModel = createModel();
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

        this.renderer = new GuiPlayerEntityRenderer(model.getModelDataIdentifier(), model.getGuiRenderInfo().getShowcaseAnimation());
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
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
        MatrixStack matrices = context.getMatrices();
        context.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        matrices.push();
        matrices.translate(getX() + getWidth() - (getWidth() / 4f), getY() + ((float) height / 1.6), 50);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateY((float) (Math.PI * 0.25f));
        quaternionf.mul(quaternionf2);
        matrices.multiply(quaternionf);
        float size = getHeight() / 4f;
        if (ModelShifterClient.state.isRendererEnabled() && renderer != null) {
            PlayerModel model = ModelShifterClient.state.getPlayerModel();
            assert model != null;
            Consumer<MatrixStack> tweakFunction = model.getGuiRenderInfo().getShowcaseRenderTweakFunction();
            matrices.scale(size, size, -size);
            if (tweakFunction != null)
                tweakFunction.accept(matrices);
            renderer.setRenderColor(255, 255, 255, 255);
            renderer.render(skinTexture, 0, 0, matrices, context.getVertexConsumers(), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE);
        } else if (!ModelShifterClient.state.isRendererEnabled() && playerEntityModel != null) {
            size /= 1.2f;
            matrices.scale(size, size, -size);
            matrices.translate(0, 1.4f, 0);
            matrices.multiply(new Quaternionf().rotateZ((float) Math.PI));
            VertexConsumerProvider.Immediate vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            int overlay = OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(false));
            //? <1.21 {
            /*playerEntityModel.render(matrices,
                    vertexConsumer.getBuffer(RenderLayer.getEntityTranslucent(skinTexture)),
                    LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                    overlay,
                    1f, 1f, 1f, 1f);
            *///?} else {
            playerEntityModel.render(matrices,
                    vertexConsumer.getBuffer(RenderLayer.getEntityTranslucent(skinTexture)),
                    LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                    overlay, -1);
            //?}
        }

        context.draw();
        matrices.pop();
        context.disableScissor();
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
}