package com.codingcat.modelshifter.client.mixin.screen;

import com.codingcat.modelshifter.client.gui.screen.ModelSelectionScreen;
import com.codingcat.modelshifter.client.gui.widget.ModelShifterButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Shadow
    @Final
    private GameOptions settings;

    @Shadow
    @Final
    private static Text SKIN_CUSTOMIZATION_TEXT;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private static final Text MODEL_SHIFTER_BTN = Text.translatable("modelshifter.button.open_screen");

    @Unique
    private final ModelShifterButtonWidget BUTTON = new ModelShifterButtonWidget(0, 0, MODEL_SHIFTER_BTN, false, button ->
            Objects.requireNonNull(client).setScreen(new ModelSelectionScreen(null, this, this.settings)));

    @Inject(
            at = @At(value = "RETURN"),
            method = "init"
    )
    public void injectButton(CallbackInfo ci) {
        setButtonPos();
        this.addDrawableChild(BUTTON);
    }

    @Inject(
            at = @At(value = "RETURN"),
            method = "createTopRightButton"
    )
    public void injectButton(CallbackInfoReturnable<Widget> cir) {
        setButtonPos();
    }

    @Unique
    private void setButtonPos() {
        Optional<ButtonWidget> widget = this.children().stream()
                .filter(child -> child instanceof ButtonWidget)
                .map(child -> (ButtonWidget) child)
                .filter(btn -> btn.getMessage().equals(SKIN_CUSTOMIZATION_TEXT))
                .findFirst();

        if (widget.isEmpty()) return;

        ButtonWidget skinBtn = widget.get();
        BUTTON.setX(skinBtn.getX() - BUTTON.getWidth() - 3);
        BUTTON.setY(skinBtn.getY());
    }
}

