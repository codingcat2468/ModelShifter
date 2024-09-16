package com.codingcat.modelshifter.client.gui.widget.entry;

import com.codingcat.modelshifter.client.impl.config.ConfigPlayerOverride;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class PlayerOverridesListWidgetEntry extends AlwaysSelectedEntryListWidget.Entry<PlayerOverridesListWidgetEntry> {
    private final static Text LOADING = Text.translatable("modelshifter.text.loading");
    @NotNull
    private final ConfigPlayerOverride override;
    @NotNull
    private final MinecraftClient client;
    private final AtomicReference<Identifier> skinTexture;
    private final AtomicReference<GameProfile> profile;

    public PlayerOverridesListWidgetEntry(@NotNull MinecraftClient client, @NotNull ConfigPlayerOverride override) {
        this.client = client;
        this.override = override;
        this.skinTexture = new AtomicReference<>();
        this.profile = new AtomicReference<>();
        new Thread(this::fetchProfileAndSkin, "Profile Fetcher Thread")
                .start();
    }

    public @Nullable GameProfile getPlayerProfile() {
        return this.profile.get();
    }

    public @Nullable Identifier getSkinTexture() {
        return this.skinTexture.get();
    }

    private void fetchProfileAndSkin() {
        ProfileResult result = client.getSessionService().fetchProfile(override.player(), false);
        if (result == null) return;

        profile.set(result.profile());
        client.getSkinProvider().fetchSkinTextures(result.profile()).thenAccept(textures -> skinTexture.set(textures.texture()));
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int y2 = (entryHeight / 3) + 2;
        int x1 = x + 20;
        int y1 = y + 3;

        if (skinTexture.get() != null)
            PlayerSkinDrawer.draw(context, skinTexture.get(), x1, y1, 30);

        if (hovered)
            context.fill(x1, y1, x1 + 30, y1 + 30, 0x37FFFFFF);

        context.drawTextWithShadow(client.textRenderer,
                getPlayerName(),
                x + 60, y + y2,
                0xFFFFFF);
    }

    private Text getPlayerName() {
        return profile.get() != null ? Text.literal(profile.get().getName()) : LOADING;
    }

    @Override
    public Text getNarration() {
        return getPlayerName();
    }
}
