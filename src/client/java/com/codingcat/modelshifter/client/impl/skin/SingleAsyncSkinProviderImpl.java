package com.codingcat.modelshifter.client.impl.skin;

import com.codingcat.modelshifter.client.api.skin.SingleAsyncSkinProvider;
import com.mojang.authlib.GameProfile;
//? <=1.20.1 {
/*import com.mojang.authlib.minecraft.MinecraftProfileTexture;
*///?}
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class SingleAsyncSkinProviderImpl implements SingleAsyncSkinProvider {
    private final MinecraftClient client;
    @Nullable
    private GameProfile profile;
    private final AtomicReference<Identifier> cachedSkin;

    public SingleAsyncSkinProviderImpl() {
        this(null);
    }

    public SingleAsyncSkinProviderImpl(@Nullable GameProfile profile) {
        this.client = MinecraftClient.getInstance();
        this.profile = profile;
        this.cachedSkin = new AtomicReference<>();
    }

    @Override
    public void fetchSkin() {
        if (profile == null) {
            this.cachedSkin.set(null);
            return;
        }

        //? >1.20.1 {
        client.getSkinProvider()
                .fetchSkinTextures(this.profile)
                .thenAccept(textures -> cachedSkin.set(textures.texture()));
        //?} else {
        /*client.getSkinProvider().loadSkin(profile, (type, id, texture) -> {
            if (type != MinecraftProfileTexture.Type.SKIN) return;
            cachedSkin.set(client.getSkinProvider().loadSkin(texture, type));
        }, false);
        *///?}
    }

    @Override
    public @Nullable Identifier getSkinOrNull() {
        return this.cachedSkin.get();
    }

    @Override
    public @NotNull Identifier getDefaultSkin(@Nullable GameProfile profile) {
        GameProfile gameProfile = profile != null ? profile : this.profile;
        if (gameProfile == null)
            return DefaultSkinHelper.getTexture();

        //? >1.20.1 {
        return client.getSkinProvider().getSkinTextures(gameProfile).texture();
        //?} else {
        /*return client.getSkinProvider().loadSkin(gameProfile);
         *///?}
    }

    @Override
    public void setProfile(@NotNull GameProfile profile) {
        this.profile = profile;
    }
}
