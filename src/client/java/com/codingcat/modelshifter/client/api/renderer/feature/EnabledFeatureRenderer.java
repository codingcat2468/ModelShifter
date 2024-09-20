package com.codingcat.modelshifter.client.api.renderer.feature;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;

public record EnabledFeatureRenderer(
        @NotNull FeatureRendererType type,
        @Nullable BiConsumer<LivingEntity, MatrixStack> renderModifierConsumer
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnabledFeatureRenderer that)) return false;
        return type() == that.type();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type());
    }
}