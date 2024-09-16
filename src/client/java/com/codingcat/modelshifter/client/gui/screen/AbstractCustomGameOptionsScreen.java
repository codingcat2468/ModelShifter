package com.codingcat.modelshifter.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
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
        //? <1.21 {
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
        if (this.listWidget != null)
            this.listWidget.position(this.width, this.layout);
    }
    *///?}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
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
}
