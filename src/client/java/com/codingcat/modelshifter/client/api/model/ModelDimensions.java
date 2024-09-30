package com.codingcat.modelshifter.client.api.model;

public record ModelDimensions(
        float width,
        float height,
        float labelOffset
) {
    public ModelDimensions(float width, float height) {
        this(width, height, 0f);
    }
}
