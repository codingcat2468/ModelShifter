package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererStates;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import com.codingcat.modelshifter.client.api.renderer.feature.FeatureRendererType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Set;

public class ArmadilloPlayerModel extends PlayerModel {
    public ArmadilloPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "armadillo_player"), Set.of("bug_finder"));
    }

    @Override
    protected @NotNull FeatureRendererStates createFeatureRendererStates() {
        return new FeatureRendererStates()
                .add(FeatureRendererType.HELD_ITEM, ArmadilloPlayerModel::modifyHeldItemRendering)
                .add(FeatureRendererType.ELYTRA, ArmadilloPlayerModel::modifyElytraRendering)
                .add(FeatureRendererType.TRIDENT_RIPTIDE);
    }

    @Override
    protected @NotNull GuiRenderInfo createGuiRenderInfo() {
        return new GuiRenderInfo()
                .setButtonRenderTweakFunction(ArmadilloPlayerModel::modifyGuiButtonRendering);
    }

    private static void modifyGuiButtonRendering(MatrixStack matrixStack) {
        matrixStack.scale(1.3f,1.3f,1.3f);
    }

    private static void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.25f,0.4f,-0.5f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.1f);
    }

    private static void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {
        Quaternionf quaternionf = new Quaternionf().rotateX((float) Math.PI * 0.5f);
        matrixStack.multiply(quaternionf);
        matrixStack.scale(0.7f, 0.7f, 0.7f);
        matrixStack.translate(0f, -0.46f, -1.34f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,-0.2f,0f);
    }
}
