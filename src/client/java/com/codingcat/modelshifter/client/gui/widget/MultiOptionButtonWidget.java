package com.codingcat.modelshifter.client.gui.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MultiOptionButtonWidget<T extends Enum<?>> extends ButtonWidget {
    private final Function<T, Text> saveState;
    private int selectedOption;
    private final List<T> options;
    private final Text optionName;

    public MultiOptionButtonWidget(int x, int y, int width, int height, Text optionName, List<T> options, Supplier<T> obtainInitialState, Function<T, Text> saveState) {
        super(x, y, width, height, Text.empty(), null, DEFAULT_NARRATION_SUPPLIER);
        this.options = options;
        this.saveState = saveState;
        this.optionName = optionName;
        T initialState = obtainInitialState.get();
        int idx = options.indexOf(initialState);
        this.selectedOption = idx != -1 ? idx : 0;
        this.setOption(initialState);
    }

    @Override
    public void onPress() {
        selectedOption++;
        if (selectedOption >= options.size())
            selectedOption = 0;

        this.setOption(options.get(selectedOption));
    }

    private void setOption(T id) {
        Text text = saveState.apply(id);
        String msg = String.format("%s: %s", optionName.getString(), text.getString());
        this.setMessage(Text.literal(msg));
    }
}
