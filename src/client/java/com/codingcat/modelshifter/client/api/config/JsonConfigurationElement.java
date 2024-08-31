package com.codingcat.modelshifter.client.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonConfigurationElement {
    String propertyName();
}
