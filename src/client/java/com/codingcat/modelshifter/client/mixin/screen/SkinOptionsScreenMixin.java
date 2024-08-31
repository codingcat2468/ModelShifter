package com.codingcat.modelshifter.client.mixin.screen;

import com.codingcat.modelshifter.client.gui.screen.ModelSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends GameOptionsScreen {
    //? <1.21 {
    /*private @Nullable OptionListWidget optionListWidget;

    *///?}

    @Unique
    private static final Text MODEL_SHIFTER_BTN = Text.translatable("modelshifter.button.open_screen");

    public SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Unique
    private ButtonWidget getButton() {
        assert this.client != null;
        return ButtonWidget.builder(MODEL_SHIFTER_BTN, button ->
                this.client.setScreen(new ModelSelectionScreen(this, gameOptions))).build();
    }

    //? <1.21 {
    /*@Inject(
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/option/GameOptionsScreen;init()V"
            ),
            method = "init"
    )
    public void injectButton(CallbackInfo ci) {
        if (this.optionListWidget == null || this.client == null) return;

        this.optionListWidget.addAll(List.of(getButton()));
    }
    *///?} else {
    @Inject(at = @At(value = "RETURN"),
            method = "addOptions")
    public void injectButton(CallbackInfo ci) {
        if (this.body != null)
            this.body.addAll(List.of(getButton()));
    }
    //?}
}
