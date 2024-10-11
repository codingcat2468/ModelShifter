package com.codingcat.modelshifter.client.api.skin;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SingleAsyncSkinProvider {
    default void setProfileAndFetch(GameProfile profile) {
        this.setProfile(profile);
        this.fetchSkin();
    }

    void setProfile(GameProfile profile);

    void fetchSkin();

    @NotNull
    default Identifier getSkin() {
        return this.getDefaultSkin(null);
    }

    @NotNull
    Identifier getDefaultSkin(@Nullable GameProfile profile);
}
