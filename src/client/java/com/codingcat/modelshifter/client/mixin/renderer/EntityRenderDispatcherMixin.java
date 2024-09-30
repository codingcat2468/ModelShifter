package com.codingcat.modelshifter.client.mixin.renderer;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.ModelDimensions;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Redirect(
            method = "renderFire",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getWidth()F")
    )
    public float redirectGetWidth(Entity entity) {
        ModelDimensions dimensions = getDimensions(entity);
        return dimensions != null ? dimensions.width() : entity.getWidth();
    }

    @Redirect(
            method = "renderFire",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHeight()F")
    )
    public float redirectGetHeight(Entity entity) {
        ModelDimensions dimensions = getDimensions(entity);
        return dimensions != null ? dimensions.height() : entity.getHeight();
    }

    @Redirect(
            method = "renderHitbox",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;")
    )
    private static Box redirectGetBox(Entity entity) {
        if (!ModelShifterClient.isDev) return entity.getBoundingBox();
        ModelDimensions dimensions = getDimensions(entity);
        if (dimensions == null) return entity.getBoundingBox();

        return getBox(dimensions.width(), dimensions.height(), entity.getX(), entity.getY(), entity.getZ());
    }


    @Unique
    private static Box getBox(float w, float h, double x, double y, double z) {
        float f = w / 2.0f;
        return new Box(x - (double) f, y, z - (double) f, x + (double) f, y + (double) h, z + (double) f);
    }

    @Unique
    @Nullable
    private static ModelDimensions getDimensions(Entity entity) {
        if (!(entity instanceof AbstractClientPlayerEntity clientPlayer)) return null;
        if (!ModelShifterClient.state.isRendererEnabled(clientPlayer)) return null;
        PlayerModel model = ModelShifterClient.state.getState(clientPlayer.getUuid()).getPlayerModel();
        if (model == null) return null;

        return model.getDimensions();
    }
}
