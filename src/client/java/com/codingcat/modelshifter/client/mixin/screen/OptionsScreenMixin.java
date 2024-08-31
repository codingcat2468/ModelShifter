package com.codingcat.modelshifter.client.mixin.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.gui.screen.ModelSelectionScreen;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Shadow
    @Final
    private GameOptions settings;

    @Shadow
    protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    @Shadow
    @Final
    private static Text SKIN_CUSTOMIZATION_TEXT;
    @Shadow
    @Final
    private ThreePartsLayoutWidget layout;
    @Shadow
    private @Nullable CyclingButtonWidget<Difficulty> difficultyButton;
    @Unique
    private static final Text MODEL_SHIFTER_BTN = Text.translatable("modelshifter.button.open_screen");
    @Unique
    private static final Identifier ICON_MODEL_SHIFTER = Identifier.of(ModelShifterClient.MOD_ID, "icons/modelshifter_icon");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private final TextIconButtonWidget BUTTON = TextIconButtonWidget.builder(MODEL_SHIFTER_BTN, button ->
                    Objects.requireNonNull(client).setScreen(new ModelSelectionScreen(this, this.settings)), true)
            .width(20)
            .texture(ICON_MODEL_SHIFTER, 15, 15)
            .build();

    @Inject(
            at = @At(value = "RETURN"),
            method = "init"
    )
    public void injectButton(CallbackInfo ci, @Local(ordinal = 1) DirectionalLayoutWidget directionalLayoutWidget) {
        setButtonPos();
        this.addDrawableChild(BUTTON);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
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
        BUTTON.setTooltip(Tooltip.of(MODEL_SHIFTER_BTN));
        BUTTON.setX(skinBtn.getX() - BUTTON.getWidth() - 3);
        BUTTON.setY(skinBtn.getY());
    }
}

