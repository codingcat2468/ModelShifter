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
        //? >1.20.1 {
        super(minecraftClient, width, height, y, itemHeight);
        this.setX(x);
        //?} else {
        /*super(minecraftClient, width, height, y, y + height, itemHeight);
        this.setLeftPos(x);
        *///?}
        this.overrideSelected = overrideSelected;
        //? <=1.20.4 {
        /*this.setRenderBackground(false);
        *///?}
        //? <=1.20.1 {
        /*this.setRenderHorizontalShadows(false);
        *///?}
    }

    @Override
    public void setSelected(@Nullable PlayerOverridesListWidgetEntry entry) {
        super.setSelected(entry);
        overrideSelected.accept(entry);
    }

    public void setOverrides(ArrayList<ConfigPlayerOverride> overrides) {
        this.clearEntries();
        overrides.forEach(override ->
                this.addEntry(new PlayerOverridesListWidgetEntry(client, this, override)));
    }

    //? >1.20.4 {
    @Override
    protected void drawMenuListBackground(DrawContext context) {
        super.drawMenuListBackground(context);
        this.drawNoItemsText(context);
    }

    @Override
    protected int getScrollbarX() {
        return this.getScrollbarPos();
    }

    //?} else {
    /*@Override
    protected int getScrollbarPositionX() {
        return this.getScrollbarPos();
    }

    //? <=1.20.1 {
    /^@Override
    protected void renderBackground(DrawContext context) {
        this.drawNoItemsText(context);
    }
    ^///?} else {
    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawNoItemsText(context);
        super.renderWidget(context, mouseX, mouseY, delta);
    }
    //?}
    *///?}

    private int getScrollbarPos() {
        //? >1.20.1 {
        return this.getRight() - 6;
         //?} else {
        /*return this.right - 6;
        *///?}
    }

    private boolean scrollbarVisible() {
        //? >1.20.4 {
        return isScrollbarVisible();
         //?} else {
        /*return getMaxScroll() > 0;
        *///?}
    }

    private void drawNoItemsText(DrawContext context) {
        if (getEntryCount() > 0) return;

        //? >1.20.1 {
        int x = getX();
        int y = getY();
        //?} else {
        /*int x = left;
        int y = top;
        *///?}
        PlayerShowcaseWidget.renderScaledText(
                context,
                NO_PLAYERS,
                0xC1C1C1,
                x + (width / 2d),
                y + (height / 2d),
                1.6f, true);
    }

    @Override
    public int getRowWidth() {
        //? >1.20.1 {
        return getWidth();
        //?} else {
        /*return width;
        *///?}
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        int j = this.getRowLeft() + 5;
        //? >1.20.1 {
        int right2 = this.getRight();
         //?} else {
        /*int right2 = this.right;
        *///?}
        int k = right2 - (scrollbarVisible() ? 10 : 3);
        int l = y - 2;
        int m = y + entryHeight + 2;
        context.fill(j, l, k, m, borderColor);
        context.fill(j + 1, l + 1, k - 1, m - 1, fillColor);
    }
}
