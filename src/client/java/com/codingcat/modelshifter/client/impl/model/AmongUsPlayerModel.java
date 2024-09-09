package com.codingcat.modelshifter.client.impl.model;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.codingcat.modelshifter.client.api.renderer.DisabledFeatureRenderers;
import com.codingcat.modelshifter.client.api.renderer.GuiRenderInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public class AmongUsPlayerModel extends PlayerModel {
    public AmongUsPlayerModel() {
        super(Identifier.of(ModelShifterClient.MOD_ID, "among_us_player"), Set.of("EgertSUS"), new DisabledFeatureRenderers(
                true,
                false,
                false,
                true,
                true,
                false,
                false,
                true
        ), new GuiRenderInfo());
    }

    @Override
    public void modifyHeldItemRendering(LivingEntity entity, MatrixStack matrixStack) {
        matrixStack.translate(0.3f,0.2f,0f);
        if (entity.isInSneakingPose())
            matrixStack.translate(0f,0f,-0.2f);
    }

    @Override
    public void modifyElytraRendering(LivingEntity entity, MatrixStack matrixStack) {}
}
