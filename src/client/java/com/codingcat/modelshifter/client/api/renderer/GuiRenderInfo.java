package com.codingcat.modelshifter.client.api.renderer;

import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.function.Consumer;

public class GuiRenderInfo {
    @NotNull
    private RawAnimation buttonAnimation = DefaultAnimations.RUN;
    @NotNull
    private RawAnimation showcaseAnimation = DefaultAnimations.IDLE;
    @Nullable
    private Consumer<MatrixStack> buttonRenderTweakFunction = null;
    @Nullable
    private Consumer<MatrixStack> showcaseRenderTweakFunction = null;

    public @NotNull RawAnimation getButtonAnimation() {
        return this.buttonAnimation;
    }

    public GuiRenderInfo setButtonAnimation(@NotNull RawAnimation buttonAnimation) {
        this.buttonAnimation = buttonAnimation;
        return this;
    }

    public @NotNull RawAnimation getShowcaseAnimation() {
        return this.showcaseAnimation;
    }

    public GuiRenderInfo setShowcaseAnimation(@NotNull RawAnimation showcaseAnimation) {
        this.showcaseAnimation = showcaseAnimation;
        return this;
    }

    public @Nullable Consumer<MatrixStack> getButtonRenderTweakFunction() {
        return this.buttonRenderTweakFunction;
    }

    public GuiRenderInfo setButtonRenderTweakFunction(@Nullable Consumer<MatrixStack> buttonRenderTweakFunction) {
        this.buttonRenderTweakFunction = buttonRenderTweakFunction;
        return this;
    }

    public @Nullable Consumer<MatrixStack> getShowcaseRenderTweakFunction() {
        return this.showcaseRenderTweakFunction;
    }

    public GuiRenderInfo setShowcaseRenderTweakFunction(@Nullable Consumer<MatrixStack> showcaseRenderTweakFunction) {
        this.showcaseRenderTweakFunction = showcaseRenderTweakFunction;
        return this;
    }
}
