package com.codingcat.modelshifter.client.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class Util {
    public static GameProfile getGameProfile() {
        //? >1.20.1 {
        return MinecraftClient.getInstance().getGameProfile();
         //?} else {
        /*return MinecraftClient.getInstance().getSession().getProfile();
        *///?}
    }

    public static void drawGuiTexture(DrawContext context, Identifier texture, int x, int y, int width, int height) {
        //? >1.20.1 {
        context.drawGuiTexture(texture, x, y, width, height);
        //?} else {
        /*context.drawTexture(texture.withPrefixedPath("textures/gui/sprites/").withSuffixedPath(".png"), x, y, 0, 0, width, height, width, height);
        *///?}
    }
}
