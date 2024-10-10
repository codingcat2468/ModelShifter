package com.codingcat.modelshifter.client.mixin.screen;

import com.codingcat.modelshifter.client.ModelShifterClient;
import com.codingcat.modelshifter.client.api.model.PlayerModel;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.joml.Quaternionf;
//? >1.20.1 {
import org.joml.Vector3f;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @Inject(
            //? >1.20.4 {
            method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;FFFLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V",
            //?} else if <=1.20.1 {
            /*method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;IIILorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V",
            *///?} else {
            /*method = "drawEntity(Lnet/minecraft/client/gui/DrawContext;FFILorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V",
            *///?}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;setRenderShadows(Z)V", ordinal = 0))
    private static void injectModifyGuiRendering(DrawContext context,
                                                 //? >1.20.1 {
                                                 float x, float y,
                                                 //?} else {
                                                 /*int x, int y,
                                                 *///?}
                                                 //? >1.20.4 {
                                                 float size,
                                                 //?} else {
                                                 /*int size,
                                                 *///?}
                                                 //? >1.20.1 {
                                                 Vector3f vector3f,
                                                 //?}
                                                 Quaternionf quaternionf, Quaternionf quaternionf2, LivingEntity entity, CallbackInfo ci) {
        //? >1.20.1 {
        GameProfile profile = MinecraftClient.getInstance().getGameProfile();
        //?} else {
        /*GameProfile profile = MinecraftClient.getInstance().getSession().getProfile();
        *///?}
        if (!ModelShifterClient.state.isRendererEnabled(profile.getId())) return;
        PlayerModel model = ModelShifterClient.state.getState(profile.getId()).getPlayerModel();
        if (model == null) return;

        Consumer<MatrixStack> tweakFunction = model.getGuiRenderInfo().getInventoryRenderTweakFunction();
        if (tweakFunction != null)
            tweakFunction.accept(context.getMatrices());
    }
}
