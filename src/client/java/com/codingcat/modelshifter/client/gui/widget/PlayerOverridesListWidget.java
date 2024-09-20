package com.codingcat.modelshifter.client.gui.widget;

import com.codingcat.modelshifter.client.gui.widget.entry.PlayerOverridesListWidgetEntry;
import com.codingcat.modelshifter.client.impl.config.ConfigPlayerOverride;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class PlayerOverridesListWidget extends AlwaysSelectedEntryListWidget<PlayerOverridesListWidgetEntry> {
    private static final Text NO_PLAYERS = Text.translatable("modelshifter.text.no_players");
    private final Consumer<PlayerOverridesListWidgetEntry> overrideSelected;
    public PlayerOverridesListWidget(MinecraftClient minecraftClient, int width, int height, int x, int y, int itemHeight, @NotNull Consumer<PlayerOverridesListWidgetEntry> overrideSelected) {
        super(minecraftClient, width, height, y, itemHeight);
        this.setX(x);
        this.overrideSelected = overrideSelected;
    }

    @Override
    public void setSelected(@Nullable PlayerOverridesListWidgetEntry entry) {
        super.setSelected(entry);
        overrideSelected.accept(entry);
    }

    public void setOverrides(ArrayList<ConfigPlayerOverride> overrides) {
        this.clearEntries();
        overrides.forEach(override ->
                this.addEntry(new PlayerOverridesListWidgetEntry(client, override)));
    }

    @Override
    protected void drawMenuListBackground(DrawContext context) {
        super.drawMenuListBackground(context);
        if (getEntryCount() <= 0)
            PlayerShowcaseWidget.renderScaledText(
                    context,
                    NO_PLAYERS,
                    0xC1C1C1,
                    getX() + (getWidth() / 2d),
                    getY() + (getHeight() / 2d),
                    1.6f, true);
    }

    @Override
    protected int getScrollbarX() {
        return this.getRight() - 6;
    }

    @Override
    public int getRowWidth() {
        return getWidth();
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        int j = this.getRowLeft() + 5;
        int k = this.getRight() - (isScrollbarVisible() ? 10 : 3);
        int l = y - 2;
        int m = y + entryHeight + 2;
        context.fill(j, l, k, m, borderColor);
        context.fill(j + 1, l + 1, k - 1, m - 1, fillColor);
    }
}
