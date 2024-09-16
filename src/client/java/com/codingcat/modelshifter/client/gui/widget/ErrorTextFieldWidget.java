package com.codingcat.modelshifter.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ErrorTextFieldWidget extends TextFieldWidget {
    private boolean error;
    private final Text hint;
    @Nullable
    private Consumer<String> changeListener;

    public ErrorTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text hint) {
        super(textRenderer, x, y, width, height, Text.empty());
        this.hint = hint;
        this.error = false;
        super.setChangedListener(this::onChange);
        onChange(getText());
    }

    private void onChange(String text) {
        if (this.error) {
            this.error = false;
            setEditable(true);
        }

        if (text.isBlank())
            setSuggestion(hint.getString());
        else
            setSuggestion(null);

        if (changeListener != null)
            changeListener.accept(text);
    }

    public void setError(Text error) {
        setText("");
        setEditable(false);
        setSuggestion(error.getString());
        this.error = true;
    }

    @Override
    public void setChangedListener(Consumer<String> changedListener) {
        this.changeListener = changedListener;
    }
}
