package com.codingcat.modelshifter.client.gui.widget.entry;

import com.codingcat.modelshifter.client.gui.widget.PlayerOverridesListWidget;
import com.codingcat.modelshifter.client.impl.config.ConfigPlayerOverride;
import com.mojang.authlib.GameProfile;
//? >1.20.1 {
import com.mojang.authlib.yggdrasil.ProfileResult;
//?} else {
/*import com.mojang.authlib.minecraft.MinecraftProfileTexture;
*///?}
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
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @NotNull
    private final PlayerOverridesListWidget parentWidget;
    @NotNull
    private final MinecraftClient client;
    private final AtomicReference<Identifier> skinTexture;
    private final AtomicReference<GameProfile> profile;

    public PlayerOverridesListWidgetEntry(@NotNull MinecraftClient client, @NotNull PlayerOverridesListWidget parentWidget, @NotNull ConfigPlayerOverride override) {
        this.client = client;
        this.parentWidget = parentWidget;
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
        //? >1.20.1 {
        ProfileResult result = client.getSessionService().fetchProfile(override.player(), false);
        if (result == null) return;

        profile.set(result.profile());
        client.getSkinProvider().fetchSkinTextures(result.profile()).thenAccept(textures -> skinTexture.set(textures.texture()));
        //?} else {
        /*GameProfile result = client.getSessionService().fillProfileProperties(new GameProfile(override.player(), ""), false);
        if (result == null) return;

        profile.set(result);
        client.getSkinProvider().loadSkin(result, (type, id, texture) -> {
            if (type != MinecraftProfileTexture.Type.SKIN) return;
            skinTexture.set(client.getSkinProvider().loadSkin(texture, type));
        }, false);
        *///?}
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int y2 = (entryHeight / 3) + 2;
        int x1 = x + (entryWidth / 14);
        int y1 = y + 3;

        if (skinTexture.get() != null)
            PlayerSkinDrawer.draw(context, skinTexture.get(), x1, y1, 30);

        if (hovered)
            context.fill(x1, y1, x1 + 30, y1 + 30, 0x37FFFFFF);

        context.drawTextWithShadow(client.textRenderer,
                getPlayerName(),
                x + (int) (entryWidth / 3.4f), y + y2,
                0xFFFFFF);
    }

    private Text getPlayerName() {
        return profile.get() != null ? Text.literal(profile.get().getName()) : LOADING;
    }

    @Override
    public Text getNarration() {
        return getPlayerName();
    }

    //? <=1.20.4 {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.parentWidget.setSelected(this);
        return true;
    }
    *///?}
}
