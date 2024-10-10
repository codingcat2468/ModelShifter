package com.codingcat.modelshifter.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
//? <=1.20.4 {
/*import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
*///?}
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.OptionListWidget;

public abstract class AbstractCustomGameOptionsScreen extends GameOptionsScreen {
    private OptionListWidget listWidget;

    public AbstractCustomGameOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Override
    protected void init() {
        super.init();
        this.listWidget = null;
        //? if <=1.20.4 {
        /*//? <=1.20.1 {
        this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25));
        //?} else {
        /^this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height - 64, 32, 25));
        ^///?}
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            if (this.client == null) return;

            this.client.options.write();
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 100, this.height - 27, 200, 20).build());
        *///?} else if <1.21 {
        /*this.listWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this.height, this));
         *///?}
    }

    //? >=1.21 {
    @Override
    protected void addOptions() {
    }
    //?}

    //? <1.21 {

    /*@Override
    protected void initTabNavigation() {
        super.initTabNavigation();
        //? >1.20.4 {
        /^if (this.listWidget != null)
            this.listWidget.position(this.width, this.layout);
        ^///?}
    }
    *///?}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //? <=1.20.4 {
        /*context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 13, 0xFFFFFF);
        *///?}
        this.renderBackground(context, mouseX, mouseY, delta);
        //? >=1.21 {
        if (this.body != null)
            this.body.render(context, mouseX, mouseY, delta);
        //?}
        if (this.listWidget != null)
            this.listWidget.render(context, mouseX, mouseY, delta);
        for (Element element : this.children()) {
            if (!(element instanceof Drawable drawable)) continue;
            if (element.equals(this.listWidget)) continue;
            //? >=1.21 {
            if (element.equals(this.body)) continue;
             //?}
            drawable.render(context, mouseX, mouseY, delta);
        }
    }

    //? <=1.20.4 {
    /*public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }
    *///?}
}